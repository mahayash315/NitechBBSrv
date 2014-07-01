package controllers;

import java.sql.SQLException;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() throws SQLException {
//    	User unique = new User(1L).unique();
    	
//		UserClassifier classifier = new UserClassifier();
//		classifier.classify();
//		Set<UserCluster> topClusters = classifier.getTopClusters();
//		UserCluster topCluster = topClusters.iterator().next();
//		if (topCluster != null) {
//			ItemClassifier itemClassifier = topCluster.getItemClassifier();
//			itemClassifier.train();
//			
//			BBItem item = new BBItem();
//			item.setAuthor("学生生活課（就職・キャリア支援係）");
//			item.setTitle("ジェネラルインターンシップ　第2次募集終了のお知らせ");
//			
//			itemClassifier.classify(item);
//		}
        return ok(index.render("Your new application is ready."));
    }

}
