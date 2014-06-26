package controllers;

import java.sql.SQLException;
import java.util.Set;

import models.service.bbanalyzer.UserClassifier;
import models.service.bbanalyzer.UserCluster;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
		UserClassifier classifier = new UserClassifier();
		classifier.classify();
		Set<UserCluster> topClusters = classifier.getTopClusters();
		for(UserCluster cluster : topClusters) {
			Logger.info("Applicaation#index(): "+cluster);
		}
        return ok(index.render("Your new application is ready."));
    }

}
