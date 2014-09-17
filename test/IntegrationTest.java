//import static org.fest.assertions.Assertions.*;
//import static play.test.Helpers.*;
//
//import java.io.File;
//
//import org.junit.Before;
//
//import play.Configuration;
//import play.libs.F.Callback;
//import play.test.TestBrowser;
//
//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigFactory;
//
//public class IntegrationTest {
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
//    /**
//     * add your integration test here
//     * in this example we just check if the welcome page is being shown
//     */
////    @Test
//    public void test() {
//        running(testServer(3333, fakeApplication(additionalConfigurations.asMap())), HTMLUNIT, new Callback<TestBrowser>() {
//            public void invoke(TestBrowser browser) {
//                browser.goTo("http://localhost:3333");
//                assertThat(browser.pageSource()).contains("Your new application is ready.");
//            }
//        });
//    }
//
//}
