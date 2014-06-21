package models.service.bbanalyzer;

import java.util.Map;

import models.entity.User;

public class UserCluster {

	public User centerUser;
	public int[] vector;
	public Map<UserCluster, Float> children;
	
	public UserCluster() {
	}
	
}
