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
    	
    	NitechUser user = new NitechUser("user1").unique();
		Map<Word, Double> featureVector = user.getFeatureVector(Arrays.asList(new Long[]{1L,2L,3L,4L,5L,6L,7L,8L,9L}));
		if (featureVector != null) {
			Set<Entry<Word,Double>> entrySet = featureVector.entrySet();
			for (Entry<Word,Double> entry : entrySet) {
				System.out.println(entry.getKey()+": "+entry.getValue());
			}
		} else {
			System.out.println("NULL!!");
		}
    	
        return ok(index.render("Your new application is ready."));
    }

}
