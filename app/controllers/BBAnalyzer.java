package controllers;

import models.request.bbanalyzer.BBAnalyzerRequest;
import models.response.bbanalyzer.BBAnalyzerResult;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.bbanalyzer.BBAnalyzerUtil;

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
	
	public static Result analyzeBody() {
		return ok();
	}
	
}
