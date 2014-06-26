package controllers;

import java.sql.SQLException;

import models.service.bbanalyzer.UserClassifier;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
		UserClassifier classifier = new UserClassifier();
		classifier.classify();
        return ok(index.render("Your new application is ready."));
    }

}
