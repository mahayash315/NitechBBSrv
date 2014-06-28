package models.service.bbanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.entity.BBReadHistory;
import models.entity.User;
import utils.bbanalyzer.BBAnalyzerUtil;

public class UserCluster {

	// クラスタの位置ベクトル
	public double[] vector;
	public Map<Long,Double> feature;
	
	// クラスタの一つ下の層にあるクラスタとその距離
	public Map<UserCluster, Double> children;
	
	// 識別器
	BBItemClassifier itemClassifier;
	
	
	/* コンストラクタ */
	public UserCluster() {
		feature = new HashMap<Long,Double>();
		children = new HashMap<UserCluster, Double>();
		itemClassifier = new BBItemClassifier(this);
	}
	public UserCluster(UserCluster baseCluster) {
		this();
		children.put(baseCluster, Double.valueOf(0.0));
//		updateVector();
		updateFeature();
	}
	public UserCluster(Map<UserCluster, Double> children) {
		this();
		this.children.putAll(children);
//		updateVector();
		updateFeature();
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
	 * 子クラスタ以下のすべてのクラスタを求める
	 * @return
	 */
	public Set<UserCluster> getAllClusters() {
		Set<UserCluster> clusters = new HashSet<UserCluster>();
		if (children != null) {
			for(UserCluster child : children.keySet()) {
				clusters.add(child);
				clusters.addAll(child.getAllClusters());
			}
		}
		return clusters;
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
	 * 自分と obj 間の距離(方向の差)を返す
	 * @param obj
	 * @return
	 */
	public double distance(UserCluster obj) {
		return BBAnalyzerUtil.featureDifference(feature, obj.feature);
	}
	
	/**
	 * クラスタのクラスタ中心ベクトルを子クラスタのベクトルの平均を取ることで更新
	 */
//	public void updateVector() {
//		if (children != null && 0 < children.size()) {
//			Set<UserCluster> keySet = children.keySet();
//			double childVector[] = keySet.iterator().next().vector;
//			if (childVector != null) {
//				vector = new double[childVector.length];
//				for(UserCluster child : keySet) {
//					MathUtil.vectorMultiplyAndAdd(vector, child.vector, child.getWeight());
//				}
//				MathUtil.vectorDivide(vector, getWeight());
//			}
//		}
//	}
	
	/**
	 * クラスタのクラスタ中心ベクトルを子クラスタの特徴の平均を取ることで更新
	 */
	public void updateFeature() {
		if (children != null && 0 < children.size()) {
			feature.clear();
			for(UserCluster child : children.keySet()) {
				for(Long key : child.feature.keySet()) {
					if (!feature.containsKey(key)) {
						feature.put(key, Double.valueOf(0));
					}
					feature.put(key, (feature.get(key) + (child.feature.get(key) * child.getWeight())));
				}
			}
			double weight = getWeight();
			for(Long key : feature.keySet()) {
				feature.put(key, feature.get(key) / weight);
			}
		}
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(BBAnalyzerUtil.printFeature(feature));
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
