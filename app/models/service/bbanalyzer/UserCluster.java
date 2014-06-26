package models.service.bbanalyzer;

import java.util.Map;

import utils.bbanalyzer.BBAnalyzerUtil;

public class UserCluster {

	// クラスタの位置ベクトル
	public double[] vector;
	
	// クラスタの一つ下の層にあるクラスタ
	public Map<UserCluster, Double> children;
	
	public UserCluster() {
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(BBAnalyzerUtil.printVector(vector));
		sb.append(", users=[");
		if (children != null) {
			for(UserCluster child : children.keySet()) {
				sb.append(child);
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
}
