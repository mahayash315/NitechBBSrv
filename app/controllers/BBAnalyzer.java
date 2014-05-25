package controllers;

import java.util.Set;

import models.entity.User;
import models.request.bbanalyzer.BBAnalyzerRequest;
import models.response.bbanalyzer.BBAnalyzerResult;
import models.service.BBAnalyzer.BBAnalyzerService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.bbanalyzer.BBAnalyzerUtil;

import com.avaje.ebean.Ebean;

public class BBAnalyzer extends Controller {

	
	public static Result redirectToIndex() {
		return redirect(controllers.routes.BBAnalyzer.index());
	}
	
	public static Result index() {
		return ok(views.html.bbanalyzer.index.render());
	}
	
	public static Result kuromoji() {
		Form<BBAnalyzerRequest> form = play.data.Form.form(BBAnalyzerRequest.class);
		return ok(views.html.bbanalyzer.kuromoji.render(form, null));
	}
	
	public static Result procKuromoji() {
		Form<BBAnalyzerRequest> form = play.data.Form.form(BBAnalyzerRequest.class).bindFromRequest();
		if( form.hasErrors() ) {
			return badRequest(views.html.bbanalyzer.kuromoji.render(form, null));
		}
		
		// 処理
		BBAnalyzerResult result = BBAnalyzerUtil.analyzeText(form.get().getBody());
		
		return ok(views.html.bbanalyzer.kuromoji.render(form, result));
	}
	
	public static Result analyzeBody() {
		return ok();
	}
	
	public static Result calcNaiveBayesParams() {
		
//		Ebean.beginTransaction();
//		try {
//			User user = new User("AAA").store();
//			BBWord word = new BBWord("インターンシップ").uniqueOrStore();
//			BBCategory category = new BBCategory(user, "5").uniqueOrStore();
//			
//			Logger.info("----");
//			BBNaiveBayesParam param = new BBNaiveBayesParam(user, word, category).uniqueOrStore();
//			if (param != null) {
//				param.setGaussMyu(param.getGaussMyu() + 10.0);
//				param.store();
//				Logger.info("AA");
//				
//				BBNaiveBayesParam param2 = new BBNaiveBayesParam(user, word, category).unique();
//				if (param != null) {
//					param.setPoissonLambda(param.getPoissonLambda() + 10.0);
//					param.store();
//					Logger.info("AA");
//				}
//			}
//			
//			Ebean.commitTransaction();
//		} catch (Exception e) {
//			Ebean.rollbackTransaction();
//			e.printStackTrace();
//			return internalServerError(e.getLocalizedMessage());
//		} finally {
//			Ebean.endTransaction();
//		}
//		return ok("AA");
		
			
		// ユーザ一覧を取得
		Set<User> users = new User().findSet();
		
		// 各ユーザのパラメータを計算
		for(User user : users) {
			Ebean.beginTransaction();
			try {
				// ベイズ推定用パラメータの設定
				BBAnalyzerService.use().train(user);
				
				Ebean.commitTransaction();
			} catch (Exception e) {
				Ebean.rollbackTransaction();
				e.printStackTrace();
				return internalServerError();
			} finally {
				Ebean.endTransaction();
			}
		}
		
		return ok();
	}
}
