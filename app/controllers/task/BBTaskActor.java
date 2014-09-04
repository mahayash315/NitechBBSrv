package controllers.task;

import java.util.List;

import models.entity.Configuration;
import models.entity.NitechUser;
import models.entity.bb.Post;
import models.service.bb.PostClassifier;
import models.service.bb.UserClassifier;
import models.setting.BBSetting;
import akka.actor.UntypedActor;

public class BBTaskActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("Call")) {

	    	UserClassifier userClassifier = new UserClassifier();
	    	PostClassifier postClassifier = new PostClassifier();
	    	
	    	// K-Means クラスタを初期化する
	    	Boolean requireInit = Configuration.getBoolean(BBSetting.CONFIGURATION_KEY_REQUIRE_USER_CLUSTER_INITIALIZATION, true);
	    	if (requireInit) {
	    		userClassifier.initClusters();
	    		Configuration.putBoolean(BBSetting.CONFIGURATION_KEY_REQUIRE_USER_CLUSTER_INITIALIZATION, false);
	    	}
	    	
	    	// ユーザを分類する
	    	userClassifier.classify();
	    	
	    	// 掲示の特徴量を更新
	    	calcPostFeatures(postClassifier);
	    	
	    	// 掲示距離を計算する
	    	postClassifier.calcPostDistances();
	    	
	    	// パラメータを学習する
	    	postClassifier.train();
	    	
	    	// 掲示を分類する
	    	List<NitechUser> users = new NitechUser().findList();
	    	for (NitechUser user : users) {
	    		List<Post> posts = user.findPossessingPosts();
	    		for (Post post : posts) {
	    			postClassifier.estimate(user, post);
	    		}
	    	}
			
		} else {
			unhandled(message);
		}
		
		BBTaskActorBase.getInstance().start();
	}
	
	
	
	/**
	 * 全掲示の掲示者、件名から含まれる単語を取り出す
	 */
	public void calcPostFeatures(PostClassifier classifier) {
		// 単語リストの最終更新日時
		long lastModified = Configuration.getLong(BBSetting.CONFIGURATION_KEY_WORD_LIST_LAST_MODIFIED, 0L);
		
		// 各掲示の特徴量算出結果が古ければ更新
		List<Post> posts = new Post().findList();
		for (Post post : posts) {
			long lastUpdated = post.getLastModified().getTime();
			if (lastUpdated < lastModified) {
				classifier.calcPostFeature(post);
			}
		}
	}

}
