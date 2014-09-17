//
//import static org.fest.assertions.Assertions.*;
//import static play.test.Helpers.*;
//
//import java.io.File;
//import java.util.List;
//
//import models.entity.NitechUser;
//import models.entity.bb.Post;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import play.Configuration;
//
//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigFactory;
//
//
//public class BBTest {
//	private Configuration additionalConfigurations;
//	
//	/**
//	 * テスト用設定ファイルを読み込む
//	 * fakeApplication(additionalConfigurations.asMap()) として使う
//	 */
//	@Before
//	public void initialize(){
//	    Config additionalConfig = ConfigFactory.parseFile(new File("conf/application-test.conf"));
//	    additionalConfigurations = new Configuration(additionalConfig);
//	}
//
//	@Test
//	public void test1() {
//		running(fakeApplication(additionalConfigurations.asMap()), new Runnable() {
//			public void run() {
//				NitechUser user = new NitechUser("testUser1").uniqueOrStore();
//				assertThat(user).isNotNull();
//				List<Post> posts = user.findPossessingPosts();
//				assertThat(posts).isNotNull();
//				List<NitechUser> users = new NitechUser().findList();
//				assertThat(users).isNotNull();
//				user.delete();
//				assertThat(new NitechUser("testUser1").unique()).isNull();
//			}
//		});
//	}
//	
//}
