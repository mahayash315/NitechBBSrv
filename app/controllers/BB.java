package controllers;

import java.util.Date;
import java.util.List;

import models.entity.Configuration;
import models.entity.NitechUser;
import models.entity.bb.Post;
import models.service.bb.PostClassifier;
import models.service.bb.UserClassifier;
import models.setting.BBSetting;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;

public class BB extends Controller {
	
	public static Result redirectToIndex() {
		return redirect(controllers.routes.BB.index());
	}

	public static Result index() {
		return ok(views.html.bb.index.render());
	}
	
	public static Result init() {
    	UserClassifier userClassifier = new UserClassifier();
    	
    	// K-Means クラスタを初期化する
		userClassifier.initClusters();
		Configuration.putBoolean(BBSetting.CONFIGURATION_KEY_REQUIRE_USER_CLUSTER_INITIALIZATION, false);
		
		return ok("OK");
	}
	
	public static Result extract() {
    	final PostClassifier postClassifier = new PostClassifier();
    	
		// 単語リストの最終更新日時
		long lastWordListModified = Configuration.getLong(BBSetting.CONFIGURATION_KEY_WORD_LIST_LAST_MODIFIED, 0L);
		Logger.info("BB#extract(): word list lastModified="+lastWordListModified);
		
		// 各掲示の特徴量算出結果が古ければ更新
		List<Post> posts = new Post().findList();
		for (final Post post : posts) {
			Date lastSampledDate = post.getLastSampled();
			long lastSampled = (lastSampledDate == null) ? 0 : lastSampledDate.getTime();
			Logger.info("BB#extract(): post="+post+", lastSampled="+lastSampled);
			if (lastSampled < lastWordListModified) {
				Ebean.execute(new TxRunnable() {
					@Override
					public void run() {
						postClassifier.calcPostFeature(post);
					}
				});
			}
		}
		Logger.info("BB#extract(): done updating features");
    	
    	// 掲示距離を計算する
    	postClassifier.calcPostDistances();
    	
    	return ok("OK");
	}
	
	public static Result train() {
    	PostClassifier postClassifier = new PostClassifier();
    	
    	// パラメータを学習する
    	postClassifier.train();
    	
		return ok("OK");
	}
	
	public static Result classify() {
    	UserClassifier userClassifier = new UserClassifier();
    	
    	// ユーザを分類する
    	userClassifier.classify();
    	
    	return ok("OK");
	}
	
	public static Result estimate() {
    	final PostClassifier postClassifier = new PostClassifier();

    	// 掲示を分類する
    	List<NitechUser> users = new NitechUser().findList();
    	for (final NitechUser user : users) {
    		List<Post> posts = user.findPossessingPosts();
    		for (final Post post : posts) {
    			Ebean.execute(new TxRunnable() {
					@Override
					public void run() {
		    			postClassifier.estimate(user, post);
					}
				});
    		}
    	}
    	
    	return ok("OK");
	}
	
	public static Result process() {
		extract();
		train();
		classify();
		estimate();
		
		return ok("OK");
	}
}
