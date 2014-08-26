package models.service.bb;

import models.entity.NitechUser;
import models.entity.bb.Post;
import play.Logger;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.Ebean;


public class PostClassifier {

	public static PostClassifier use() {
		return new PostClassifier();
	}
	
	
	/**
	 * 掲示のクラスを予測する
	 * @param nitechUser
	 * @param post
	 */
	public void estimate(NitechUser nitechUser, Post post) {
		if (nitechUser != null && post != null) {
			Ebean.beginTransaction();
			try {
				CallableSql cSql = Ebean.createCallableSql("{call Estimate(?,?)}")
										.setParameter(1, nitechUser.getId())
										.setParameter(2, post.getId());
				Ebean.execute(cSql);
	            Ebean.execute(Ebean.createCallableSql("commit;"));
				Ebean.commitTransaction();
			} catch (Exception e) {
				e.printStackTrace();
				Logger.error(PostClassifier.class.getName()+"#estimate()", e);
				Ebean.rollbackTransaction();
			} finally {
				Ebean.endTransaction();
			}
		}
	}
	
}
