package models.service.bbanalyzer;

import models.entity.User;
import utils.bbanalyzer.BBAnalyzerUtil;

public class AtomUserCluster extends UserCluster {

	public User user;
	
	@Override
	public void updateVector() {
		// do nothing
	}
	
	@Override
	public double getWeight() {
		return 1.0;
	}
	
	/* toString() */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("user=");
		sb.append(user);
		sb.append(", ");
		sb.append(BBAnalyzerUtil.printVector(vector));
		return sb.toString();
	}
}
