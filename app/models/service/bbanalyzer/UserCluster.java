package models.service.bbanalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import utils.bbanalyzer.BBAnalyzerUtil;

public class UserCluster {

	// クラスタの位置ベクトル
	public double[] vector;
	
	// クラスタの一つ下の層にあるクラスタ
	public Map<UserCluster, Double> children;
	
	public UserCluster() {
		children = new HashMap<UserCluster, Double>();
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
					BBAnalyzerUtil.vectorAdd(vector, child.vector);
				}
				BBAnalyzerUtil.vectorDivide(vector, Double.valueOf(children.size()));
			}
		}
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(BBAnalyzerUtil.printVector(vector));
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
	
}
