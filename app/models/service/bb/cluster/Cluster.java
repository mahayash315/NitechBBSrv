package models.service.bb.cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import models.entity.bb.Word;
import utils.bbanalyzer.LogUtil;

public class Cluster {
	private static final double MAX_DISTANCE = 2.0;
	private static final int MAX_KMEANS_COUNT = 10;
	private static final int MIN_KMEANS_CHANGE = 0;

	// クラスタの中心ベクトル
	protected HashMap<Word,Double> vector;
	// 親クラスタ
	protected Cluster parent;
	// 子クラスタと自分との距離
	protected HashMap<Cluster,Double> children;
	
	public Cluster() {
		vector = new HashMap<Word,Double>();
	}
	public Cluster(Cluster child) {
		vector = new HashMap<Word,Double>();
		children = new HashMap<Cluster,Double>();
		addChild(child, 0);
	}
	
	/**
	 * 親クラスタを設定する
	 * @param parent
	 */
	public void setParent(Cluster parent) {
		this.parent = parent;
	}
	
	/**
	 * 親クラスタを返す
	 * @param parent
	 */
	public Cluster getParent() {
		return parent;
	}
	
	/**
	 * 子クラスタを追加する
	 * @param child
	 * @param distance
	 */
	public void addChild(Cluster child, double distance) {
		if (child != null) {
			children.put(child, Double.valueOf(distance));
			child.setParent(this);
		}
	}
	
	/**
	 * 子クラスタを追加する
	 * @param children
	 */
	public void addChildren(Map<Cluster, Double> children) {
		this.children.putAll(children);
		for(Cluster child : children.keySet()) {
			child.setParent(this);
		}
	}
	
	/**
	 * 子クラスタをすべて削除する
	 */
	public void clearChildren() {
		children.clear();
	}
	
	/**
	 * クラスタの重みを返す
	 * @return
	 */
	public double getWeight() {
		double weight = 1;
		if (children != null) {
			for(Cluster child : children.keySet()) {
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
	public double distance(Cluster obj) {
		return vectorDifference(vector, obj.vector);
	}
	
	/**
	 * クラスタのクラスタ中心ベクトルを子クラスタの特徴の平均を取ることで更新
	 */
	public void updateVector() {
		if (children != null && 0 < children.size()) {
			vector.clear();
			
			// 子クラスタの特徴の平均を取る
			for(Cluster child : children.keySet()) {
				for(Word key : child.vector.keySet()) {
					if (!vector.containsKey(key)) {
						vector.put(key, Double.valueOf(0));
					}
					vector.put(key, (vector.get(key) + (child.vector.get(key) * child.getWeight())));
				}
			}
			double weight = getWeight();
			for(Word key : vector.keySet()) {
				vector.put(key, vector.get(key) / weight);
			}
			
			// 各子クラスたとの距離を再計算
			for(Cluster child : children.keySet()) {
				children.put(child, Double.valueOf(distance(child)));
			}
		}
	}
	
	
	public static HashSet<Cluster> kmeans(HashSet<Cluster> children, int k) {
		HashSet<Cluster> parents = initKMeans(children, k);
		
		Map<Cluster, Cluster> prevParents = new HashMap<Cluster, Cluster>();
		
		// 現在の状態を保存
		for(Cluster child : children) {
			prevParents.put(child, child.getParent());
		}
		
		// make CLUSTER_SIZES[depth-1] clusters in parents
		// for each cluster in parents
		// 		for each cluster in children
		// 			calculate distance (cosin) from parent
		//			merge the clusters which are the closest to the clusters in parents
		
		// depth 層のクラスタリング
		
		// for i=1,2,...,n
		// 		for each cluster in children
		// 			insert this to the closest parent cluster
		//		if (i<n)
		//			for each cluster in parents
		//				update its center vector so that it refers to
		//				the average vector of its child clusters
		
		int count = 0;
		int changed = 0;
		do {
			LogUtil.info("UserClassifier#doClassify():   trial "+count);
			changed = 0;
			
			for(Cluster parent : parents) {
				parent.clearChildren();
			}
			
			for(Cluster child : children) {
				double minimumDistance = MAX_DISTANCE;
				Cluster parent = null;
				for(Cluster candidate : parents) {
					double d = child.distance(candidate);
					if (d < minimumDistance) {
						minimumDistance = d;
						parent = candidate;
					}
				}
				parent.addChild(child, minimumDistance);
				
				Cluster prev = prevParents.get(child);
				if (prev != null && !prev.equals(parent)) {
					++changed;
				}
				prevParents.put(child, parent);
			}
			
			// 各 parent クラスタのクラスタ中心を更新
			for(Cluster parent : parents) {
				parent.updateVector();
			}
			
			++count;
		} while (MIN_KMEANS_CHANGE < changed && count < MAX_KMEANS_COUNT);
		
		return parents;
	}
	
	/**
	 * 親クラスタ中心を k 個作る
	 * @param depth
	 */
	private static HashSet<Cluster> initKMeans(HashSet<Cluster> children, int k) {
		HashSet<Cluster> parents = new HashSet<Cluster>(k);
		HashSet<Cluster> selected = new HashSet<Cluster>(k);
		Cluster clusters[] = new Cluster[k];
		Cluster furthestCluster = null;
		double furthestDistance = 0;
		
		// クラスタ中心を1つ決める
		Cluster firstCluster = children.iterator().next();
		clusters[0] = new Cluster(firstCluster);
		parents.add(clusters[0]);
		selected.add(firstCluster);
		
		// num 個のクラスタ中心まで増やす
		for(int i = 1; i < k; ++i) {
			// どの親クラスタからも一番遠い子クラスタを見つける
			furthestCluster = null;
			furthestDistance = 0;
			for(Cluster child : children) {
				// すでに親なら除外
				if (selected.contains(child)) {
					continue;
				}
				// calculate the distance from the nearest parent cluster
				double minimumDistance = MAX_DISTANCE;
				for(Cluster parent : parents) {
					double d = child.distance(parent);
					if (d < minimumDistance) {
						minimumDistance = d;
					}
				}
				if (furthestDistance <= minimumDistance) {
					furthestCluster = child;
					furthestDistance = minimumDistance;
				}
			}
			
			// furthestCluster を次のクラスタ中心 cluster[i] とする
			clusters[i] = new Cluster(furthestCluster);
			parents.add(clusters[i]);
			selected.add(furthestCluster);
		}
		
		return parents;
	}

	
	private static double vectorMultiply(Map<?,Double> v1, Map<?,Double> v2) {
		double res = 0;
		for(Object key : v1.keySet()) {
			if (v2.containsKey(key)) {
				res = res + (v1.get(key) * v2.get(key));
			}
		}
		return res;
	}
	
	private static double vectorSize(Map<?,Double> vector) {
		double size = 0;
		for(Entry<?, Double> entry : vector.entrySet()) {
			double d = entry.getValue();
			size = size + d*d;
		}
		size = Math.sqrt(size);
		return size;
	}
	
	private static double vectorDifference(Map<?,Double> v1, Map<?,Double> v2) {
		double cosin = vectorMultiply(v1, v2) / (vectorSize(v1) * vectorSize(v2));
		return (1.0 - cosin);
	}
}
