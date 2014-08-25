package controllers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import models.entity.NitechUser;
import models.entity.bb.Word;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
        return ok(index.render("Your new application is ready."));
    }

}
