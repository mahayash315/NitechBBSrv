package models.service.bbanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.entity.BBReadHistory;
import models.entity.User;
import utils.bbanalyzer.MathUtil;

public class UserCluster {

	// クラスタの位置ベクトル
	public double[] vector;
	
	// クラスタの一つ下の層にあるクラスタ
	public Map<UserCluster, Double> children;
	
	// 識別器
	BBItemClassifier itemClassifier;
	
	
	/* コンストラクタ */
	public UserCluster() {
		children = new HashMap<UserCluster, Double>();
		itemClassifier = new BBItemClassifier(this);
	}
	public UserCluster(UserCluster baseCluster) {
		this();
		children.put(baseCluster, Double.valueOf(0.0));
		updateVector();
	}
	public UserCluster(Map<UserCluster, Double> children) {
		this();
		this.children.putAll(children);
		updateVector();
	}
	
	
	/* インスタンスメソッド */
	/**
	 * クラスタ内に存在するすべてのユーザを取得する
	 * @return
	 */
	public Set<User> getAllUsers() {
		Set<User> users = new HashSet<User>();
		if (children != null) {
			for(UserCluster child : children.keySet()) {
				users.addAll(child.getAllUsers());
			}
		}
		return users;
	}
	
	/**
	 * クラスタ内に存在するすべてのユーザの掲示閲覧履歴を取得する
	 * @param users
	 * @param minOpenTime 最小の openTime, 指定しない場合 null
	 * @return
	 */
	public Set<BBReadHistory> getAllReadHistories(Set<User> users, Long minOpenTime) {
		Set<BBReadHistory> histories = new HashSet<BBReadHistory>();
		for(User user : users) {
			histories.addAll(new BBReadHistory().findSetForUser(user, minOpenTime));
		}
		return histories;
	}
	/**
	 * クラスタ内に存在するすべてのユーザの掲示閲覧履歴を取得する
	 * @param minOpenTime 最小の openTime, 指定しない場合 null
	 * @return
	 */
	public Set<BBReadHistory> getAllReadHistories(Long minOpenTime) {
		return getAllReadHistories(getAllUsers(), minOpenTime);
	}
	
	/**
	 * クラスタの重みを返す
	 * @return
	 */
	public double getWeight() {
		double weight = 0;
		if (children != null) {
			for(UserCluster child : children.keySet()) {
				weight = weight + child.getWeight();
			}
		}
		return weight;
	}
	
	/**
	 * クラスタのクラスタ中心ベクトルを子クラスタのベクトルの平均を取ることで更新
	 */
	public void updateVector() {
		if (children != null && 0 < children.size()) {
			Set<UserCluster> keySet = children.keySet();
			double childVector[] = keySet.iterator().next().vector;
			if (childVector != null) {
				vector = new double[childVector.length];
				for(UserCluster child : keySet) {
					MathUtil.vectorMultiplyAndAdd(vector, child.vector, child.getWeight());
				}
				MathUtil.vectorDivide(vector, getWeight());
			}
		}
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(MathUtil.printVector(vector));
		if (children != null) {
			sb.append(", users=[");
			if (0 < children.size()) {
				for(UserCluster child : children.keySet()) {
					sb.append(" ");
					sb.append(child);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append(" ");
			}
			sb.append("]");
		}
		return sb.toString();
	}
	
	
	/* getter, setter */
	public BBItemClassifier getItemClassifier() {
		return itemClassifier;
	}
}
