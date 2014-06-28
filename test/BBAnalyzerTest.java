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
import utils.bbanalyzer.MathUtil;

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
	
	
	@Test
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
					
					BBItemClassifier itemClassifier = topCluster.getItemClassifier();
					itemClassifier.train();
					
					BBItem item = new BBItem("2014-06-25", "4");
					item.setAuthor("学生生活課（就職・キャリア支援係）");
					item.setTitle("ジェネラルインターンシップ　第2次募集終了のお知らせ");
					
					itemClassifier.classify(item);
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				} finally {
					Logger.info("BBAnalyzerTest#test1(): end");
				}
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
			sb.append(MathUtil.printVector(cluster.vector));
			sprintClusters(sb, reverseDepth+1, cluster.children);
		}
	}
}
