package controllers;

import java.sql.SQLException;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
        return ok(index.render("Your new application is ready."));
    }

}
