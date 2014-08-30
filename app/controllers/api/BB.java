package controllers.api;

import models.request.api.bb.AddPossessionsRequest;
import models.request.api.bb.StoreHistoriesRequest;
import models.response.api.bb.AddPossessionResponse;
import models.response.api.bb.DeletePossessionResponse;
import models.response.api.bb.RelevantsResponse;
import models.response.api.bb.StoreHistoriesResponse;
import models.response.api.bb.SuggestionsResponse;
import models.response.api.bb.WordListResponse;
import models.service.api.bb.BBService;
import models.service.api.bb.BBService.InvalidParameterException;
import models.setting.api.bb.BBStatusSetting;
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
	public static Result wordList() {
		WordListResponse response = null;
		
		try {
			response = BBService.use().procWordList();
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new WordListResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new WordListResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
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
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new AddPossessionResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new AddPossessionResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 掲示を消すアクション (invalidate)
	 * @return
	 */
	public static Result deletePossessions(String hashedNitechId, String idDates, String idIndexes) {
		DeletePossessionResponse response = null;
		
		try {
			response = BBService.use().procDeletePossessions(hashedNitechId, idDates, idIndexes);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new DeletePossessionResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new DeletePossessionResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 掲示閲覧履歴を追加するアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result storeHistories() {
		StoreHistoriesResponse response = null;
		
		try {
			StoreHistoriesRequest json = Json.fromJson(request().body().asJson(), StoreHistoriesRequest.class);
			response = BBService.use().procStoreHistories(json);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new StoreHistoriesResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new StoreHistoriesResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * お勧め掲示を返すアクション
	 * @return
	 */
	public static Result suggestions(String hashedNitechId) {
		SuggestionsResponse response = null;
		
		try {
			response = BBService.use().procSuggestions(hashedNitechId);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new SuggestionsResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new SuggestionsResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 関連掲示を返すアクション
	 * @return
	 */
	public static Result relevants(String idDate, int idIndex) {
		RelevantsResponse response = null;
		
		try {
			response = BBService.use().procRelevants(idDate, idIndex);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new RelevantsResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new RelevantsResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
}
