package controllers;

import java.sql.SQLException;
import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Post;
import models.service.bb.PostClassifier;
import models.service.bb.UserClassifier;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
    	
//    	NitechUser nitechUser = new NitechUser("user1").unique();
//    	Post post = new Post("2014-08-24", 6).unique();
//    	Possession possession = new Possession(nitechUser, post).unique();
//    	System.out.println("possession.class="+possession.getClazz());
    	
    	
    	UserClassifier userClassifier = new UserClassifier();
    	PostClassifier postClassifier = new PostClassifier();
    	
//    	userClassifier.initClusters();
    	postClassifier.calcPostDistances();
    	postClassifier.train();
    	
    	List<NitechUser> users = new NitechUser().findList();
    	for (NitechUser user : users) {
    		List<Post> posts = user.findPossessingPosts();
    		for (Post post : posts) {
    			postClassifier.estimate(user, post);
    		}
    	}
    	
        return ok(index.render("Your new application is ready."));
    }

}
