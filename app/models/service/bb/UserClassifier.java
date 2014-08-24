package models.service.bb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.entity.bb.UserCluster;
import models.entity.bb.UserClusterVector;
import models.entity.bb.Word;
import models.setting.BBSetting;

public class UserClassifier {

	// 各層のクラスタ
	protected HashMap<Integer,Set<Cluster>> clusters;
	// 単語リスト
	protected List<Word> words;
	// DBエントリとクラスタの割り当てマップ
	private HashMap<UserCluster,Cluster> clusterMap;
	
	
	
	
	
	
	
	
	
	private void load() {
		clusters = new HashMap<Integer,Set<Cluster>>();
		words = new Word().findList();
		clusterMap = new HashMap<UserCluster,Cluster>();
		
		// 各層のユーザクラスタをクラスタに変換
		for (int i = BBSetting.CLUSTER_DEPTH; 0 <= i; --i) {
			Set<Cluster> children = new HashSet<Cluster>();
			clusters.put(Integer.valueOf(i), children);
			
			// i 層のクラスタの DB エントリを取得
			List<UserCluster> uChildren = new UserCluster(i).findList();
			for (UserCluster uChild : uChildren) {
				Cluster child = new Cluster(uChild);
				clusterMap.put(uChild, child);
				children.add(child);
				
				// UserCluster の持つパラメータを Cluster に渡す
				for (Word word : words) {
					child.vector.put(word, Double.valueOf(0));
				}
				for (UserClusterVector v : uChild.getVector()) {
					child.vector.put(v.word, Double.valueOf(v.value));
				}
				UserCluster uParent = uChild.getParent();
				if (uParent != null) {
					Cluster parent = clusterMap.get(uParent);
					if (parent != null) {
						parent.addChild(child);
					}
				}
			}
		}
	}
	
	private void save() {
		if (clusters != null) {
			for (int i = BBSetting.CLUSTER_DEPTH; 0 <= i; --i) {
				Set<Cluster> children = clusters.get(Integer.valueOf(i));
				for (Cluster child : children) {
					UserCluster uChild = child.getUserCluster();
					if (uChild != null) {
						uChild.setDepth(i);
						uChild.setParent(child.parent.getUserCluster());
//						uChild.setVector(vector); // TODO
						uChild.store();
					}
				}
			}
		}
	}
}
