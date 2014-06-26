import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import models.entity.User;
import models.service.bbanalyzer.BBAnalyzerService;
import models.service.bbanalyzer.UserClassifier;
import models.service.bbanalyzer.UserCluster;

import org.junit.Test;

import play.Logger;
import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;

public class BBAnalyzerTest {

	@Test
	public void test1() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Logger.info("BBAnalyzerTest#test1(): begin");
				
				User user = new User(1L).unique();
				assertThat(user).isNotNull();
				
				UserClassifier classifier = new UserClassifier();
				try {
					classifier.classify();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				Set<UserCluster> topClusters = classifier.getTopCluster();
				assertThat(topClusters).isNotNull();
				
				Map<UserCluster, Double> clusters = new HashMap<UserCluster, Double>();
				for(UserCluster cluster : topClusters) {
					clusters.put(cluster, Double.valueOf(0));
				}
				StringBuilder sb = new StringBuilder();
				printClusters(0, sb, clusters);
			}
		});
	}
	
	
	
	private void printClusters(int depth, StringBuilder sb, Map<UserCluster, Double> clusters) {
		for(UserCluster cluster : clusters.keySet()) {
			double distance = clusters.get(cluster).doubleValue();
			for(int i = 0; i < depth; ++i) {
				sb.append(" ");
			}
			sb.append(cluster.toString());
			sb.append("("+distance+")");
			sb.append("¥n");

			Map<UserCluster, Double> children = cluster.children;
			printClusters(depth+1, sb, children);
		}
	}
}
