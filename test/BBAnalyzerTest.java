import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import models.entity.BBItem;
import models.entity.User;
import models.service.bbanalyzer.AtomUserCluster;
import models.service.bbanalyzer.BBItemClassifier;
import models.service.bbanalyzer.UserClassifier;
import models.service.bbanalyzer.UserCluster;

import org.junit.Test;

import play.Logger;
import utils.bbanalyzer.BBAnalyzerUtil;

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
				
				Set<UserCluster> topClusters = classifier.getTopClusters();
				assertThat(topClusters).isNotNull();
				
				Map<UserCluster, Double> clusters = new HashMap<UserCluster, Double>();
				for(UserCluster cluster : topClusters) {
					clusters.put(cluster, Double.valueOf(0.0));
				}
				StringBuilder sb = new StringBuilder();
				sprintClusters(sb, 0, clusters);
				Logger.info("BBAnalyzerTest#test1(): "+sb.toString());
			}
		});
	}
	
	
//	@Test
	public void test2() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Logger.info("BBAnalyzerTest#test2(): begin");

				try {
					User user = new User(1L).unique();
					assertThat(user).isNotNull();
					
					UserClassifier classifier = new UserClassifier();
					try {
						classifier.classify();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					Set<UserCluster> topClusters = classifier.getTopClusters();
					assertThat(topClusters).isNotNull();
					
					UserCluster topCluster = topClusters.iterator().next();
					assertThat(topCluster).isNotNull();
					
					Set<UserCluster> allUserClusters = topCluster.getAllClusters();
					
					BBItem item = new BBItem("2014-06-25", "4");
					item.setAuthor("学生生活課（就職・キャリア支援係）");
					item.setTitle("ジェネラルインターンシップ　第2次募集終了のお知らせ");
					
					Map<UserCluster, Integer> classified = new HashMap<UserCluster, Integer>();
					for(UserCluster userCluster : allUserClusters) {
						BBItemClassifier itemClassifier = userCluster.getItemClassifier();
						itemClassifier.train();
						int classifiedTo = itemClassifier.classify(item);
						classified.put(userCluster, Integer.valueOf(classifiedTo));
					}
					
					for(UserCluster userCluster : classified.keySet()) {
						Logger.info("BBAnalyzerTest#test2(): classified item to CLASS["+classified.get(userCluster)+"]");
					}
					
					BBItemClassifier topItemClassifier = topCluster.getItemClassifier();
					topItemClassifier.train();
					int classifiedTo = topItemClassifier.classify(item);
					Logger.info("BBAnalyzerTest#test2(): classified item to CLASS["+classifiedTo+"]");
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} finally {
					Logger.info("BBAnalyzerTest#test2(): end");
				}
			}
		});
	}
	
//	@Test
	public void test3() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Map<Long, Double> feature1 = new HashMap<Long,Double>(){{
					put(Long.valueOf(3), Double.valueOf(1));
				}};
				Map<Long, Double> feature2 = new HashMap<Long,Double>(){{
					put(Long.valueOf(1), Double.valueOf(5));
					put(Long.valueOf(2), Double.valueOf(2));
					put(Long.valueOf(3), Double.valueOf(3));
				}};
				AtomUserCluster u1 = new AtomUserCluster();
				u1.feature = feature1;
				AtomUserCluster u2 = new AtomUserCluster();
				u2.feature = feature2;
				UserCluster c1 = new UserCluster(u1);
				UserCluster c2 = new UserCluster(u2);
				Logger.info("u1.feature = "+BBAnalyzerUtil.printFeature(u1.feature));
				Logger.info("c1.feature = "+BBAnalyzerUtil.printFeature(c1.feature));
				Logger.info("u2.feature = "+BBAnalyzerUtil.printFeature(u2.feature));
				Logger.info("c2.feature = "+BBAnalyzerUtil.printFeature(c2.feature));
			}
		});
	}
	
	
	
	
	
	private void sprintClusters(StringBuilder sb, int reverseDepth, Map<UserCluster, Double> clusters) {
		if (clusters == null) {
			return;
		}
		
		for(UserCluster cluster : clusters.keySet()) {
			sb.append("\n");
			for(int i=0; i<reverseDepth*2; ++i) {
				sb.append(" ");
			}
			sb.append("- ");
			
			double distance = clusters.get(cluster).doubleValue();
			
			if (cluster instanceof AtomUserCluster) {
				sb.append(((AtomUserCluster) cluster).user);
				sb.append(", ");
			}
			sb.append(BBAnalyzerUtil.printFeature(cluster.feature));
			sprintClusters(sb, reverseDepth+1, cluster.children);
		}
	}
}
