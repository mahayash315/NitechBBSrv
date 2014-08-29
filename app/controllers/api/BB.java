package controllers.api;

import models.request.api.bb.AddPossessionsRequest;
import models.response.api.bb.AddPossessionResponse;
import models.response.api.bb.WordListResponse;
import models.service.api.bb.BBService;
import models.setting.bb.api.BBStatusSetting;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.api.bb.LogUtil;

public class BB extends Controller {

	/**
	 * 単語リストを返すアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result wordList() {
		WordListResponse response = null;
		
		try {
			response = BBService.use().procWordList();
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new WordListResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 新規掲示を追加するアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addPossessions() {
		AddPossessionResponse response = null;
		
		try {
			AddPossessionsRequest json = Json.fromJson(request().body().asJson(), AddPossessionsRequest.class);
			response = BBService.use().procAddPossessions(json);
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new AddPossessionResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 掲示を消すアクション (invalidate)
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result deletePossessions() {
		return TODO;
	}
	
	/**
	 * 掲示閲覧履歴を追加するアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result storeHistories() {
		return TODO;
	}
	
	/**
	 * お勧め掲示を返すアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result suggestions() {
		return TODO;
	}
	
	/**
	 * 関連掲示を返すアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result relevants() {
		return TODO;
	}
}
