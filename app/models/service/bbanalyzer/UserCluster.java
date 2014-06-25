package models.service.bbanalyzer;

import java.util.Map;

import models.entity.User;

public class UserCluster {

	// クラスタの位置ベクトル
	public int[] vector;
	
	// クラスタの一つ下の層にあるクラスタ
	public Map<UserCluster, Double> children;
	
	public UserCluster() {
	}
	
}
