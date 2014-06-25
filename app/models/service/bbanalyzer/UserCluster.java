package models.service.bbanalyzer;

import java.util.Map;

public class UserCluster {

	// クラスタの位置ベクトル
	public double[] vector;
	
	// クラスタの一つ下の層にあるクラスタ
	public Map<UserCluster, Double> children;
	
	public UserCluster() {
	}
	
}
