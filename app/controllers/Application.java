package controllers;

import java.sql.SQLException;
import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Post;
import models.service.bb.PostClassifier;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
    	
//    	UserClassifier.use().initClusters();
    	
    	PostClassifier postClassifier = new PostClassifier();
    	
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
