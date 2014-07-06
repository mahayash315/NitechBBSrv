package models.service.bbanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.entity.BBReadHistory;
import models.entity.User;
import utils.bbanalyzer.BBAnalyzerUtil;

public class UserCluster {
	
	// クラスタのID
	public int depth;
	public long id;

	// クラスタの位置ベクトル
	public Map<Long,Double> feature;
	
	// クラスタの一つ上の層にあるクラスタとその距離
	public UserCluster parent;
	
	// クラスタの一つ下の層にあるクラスタとその距離
	public Map<UserCluster, Double> children;
	
	// 識別器
	private ItemClassifier itemClassifier;
	
	
	/* コンストラクタ */
	public UserCluster() {
		feature = new HashMap<Long,Double>();
		children = new HashMap<UserCluster, Double>();
		itemClassifier = new ItemClassifier();
	}
	public UserCluster(int depth, long id) {
		this();
		this.depth = depth;
		this.id = id;
		itemClassifier = new ItemClassifier(this);
	}
	public UserCluster(long id, UserCluster childCluster) {
		this();
		depth = childCluster.depth + 1;
		this.id = id;
		itemClassifier = new ItemClassifier(this);
		addChild(childCluster, 0.0);
		updateFeature();
	}
	public UserCluster(long id, Map<UserCluster, Double> children) {
		this();
		if (children != null && !children.isEmpty()) {
			depth = children.keySet().iterator().next().depth + 1;
		}
		this.id = id;
		addChildren(children);
		updateFeature();
	}
	
	
	/* インスタンスメソッド */
	/**
	 * 親クラスタを設定する
	 * @param parent
	 */
	public void setParent(UserCluster parent) {
		this.parent = parent;
	}
	
	/**
	 * 親クラスタを返す
	 * @param parent
	 */
	public UserCluster getParent() {
		return parent;
	}
	
	/**
	 * 子クラスタを追加する
	 * @param child
	 * @param distance
	 */
	public void addChild(UserCluster child, double distance) {
		if (child != null) {
			children.put(child, Double.valueOf(distance));
			child.setParent(this);
		}
	}
	
	/**
	 * 子クラスタを追加する
	 * @param children
	 */
	public void addChildren(Map<UserCluster, Double> children) {
		this.children.putAll(children);
		for(UserCluster child : children.keySet()) {
			child.setParent(this);
		}
	}
	
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
	 * クラスタのクラスタ中心ベクトルを子クラスタの特徴の平均を取ることで更新
	 */
	public void updateFeature() {
		if (children != null && 0 < children.size()) {
			feature.clear();
			
			// 子クラスタの特徴の平均を取る
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
			
			// 各子クラスたとの距離を再計算
			for(UserCluster child : children.keySet()) {
				children.put(child, Double.valueOf(distance(child)));
			}
		}
	}
	
	/* hashCode, equals */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (depth ^ (depth >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCluster other = (UserCluster) obj;
		if (depth != other.depth)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("depth=");
		sb.append(depth);
		sb.append(", id=");
		sb.append(id);
		sb.append(", ");
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
	public ItemClassifier getItemClassifier() {
		return itemClassifier;
	}

}
