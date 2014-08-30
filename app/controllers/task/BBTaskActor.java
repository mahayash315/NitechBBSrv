package controllers.task;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Post;
import models.service.bb.PostClassifier;
import models.service.bb.UserClassifier;
import akka.actor.UntypedActor;

public class BBTaskActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("Call")) {

	    	UserClassifier userClassifier = new UserClassifier();
	    	PostClassifier postClassifier = new PostClassifier();
	    	
	    	// K-Means クラスタを初期化する
//	    	userClassifier.initClusters();
	    	
	    	// ユーザを分類する
	    	userClassifier.classify();
	    	
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

}
