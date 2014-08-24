
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
				NitechUser user = new NitechUser("user1").unique();
				assertThat(user).isNotNull();
				List<Post> posts = user.findPossessingPosts();
				assertThat(posts).isNotNull();
				
				System.out.println("possessing "+posts.size()+" posts");
			}
		});
	}
	
}
