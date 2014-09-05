
import static org.fest.assertions.Assertions.*;
import static play.test.Helpers.*;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Post;

import org.junit.Test;


public class BBTest {

	@Test
	public void test1() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				NitechUser user = new NitechUser("testUser1").uniqueOrStore();
				assertThat(user).isNotNull();
				List<Post> posts = user.findPossessingPosts();
				assertThat(posts).isNotNull();
				List<NitechUser> users = new NitechUser().findList();
				assertThat(users).isNotNull();
				user.delete();
				assertThat(new NitechUser("testUser1").unique()).isNull();
			}
		});
	}
	
}
