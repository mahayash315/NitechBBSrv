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
	
	/**
	 * クラスタのクラスタ中心ベクトルを子クラスタのベクトルの平均を取ることで更新
	 */
	public void updateVector() {
		double vector[] = new double[this.vector.length];
		for(UserCluster child : children.keySet()) {
			BBAnalyzerUtil.vectorAdd(vector, child.vector);
		}
		BBAnalyzerUtil.vectorDivide(vector, Double.valueOf(children.size()));
		this.vector = vector;
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
