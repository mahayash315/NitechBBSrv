package controllers;

import java.sql.SQLException;

import models.request.bbanalyzer.BBAnalyzerRequest;
import models.response.bbanalyzer.BBAnalyzerResult;
import models.service.bbanalyzer.BBAnalyzerService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.bbanalyzer.BBAnalyzerUtil;
import utils.bbanalyzer.LogUtil;

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
		BBAnalyzerResult result = BBAnalyzerUtil.use().analyzeText(form.get().getBody());
		
		return ok(views.html.bbanalyzer.kuromoji.render(form, result));
	}
	
	public static Result classifyAllUsers() {
		
		try {
			BBAnalyzerService.use().classifyAllUsers();
		} catch (SQLException e) {
			LogUtil.error("BBAnalyzer#classifyAllUsers()"+e);
			return internalServerError(e.toString());
		}
		
		return ok("DONE");
	}
	
	public static Result trainAllItemClassifiers() {
		
		try {
			BBAnalyzerService.use().trainAllItemClassifiers();
		} catch (SQLException e) {
			LogUtil.error("BBAnalyzer#trainAllItemClassifiers()"+e);
			return internalServerError(e.toString());
		}
		
		return ok("DONE");
	}
	
	public static Result analyzeBody() {
		return ok();
	}
	
}
