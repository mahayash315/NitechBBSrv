package controllers.api;

import models.request.api.bb.AddPossessionsRequest;
import models.request.api.bb.OnLoginRequest;
import models.request.api.bb.StoreHistoriesRequest;
import models.request.api.bb.SyncPossessionsRequest;
import models.request.api.bb.UpdatePossessionsRequest;
import models.request.api.bb.UpdatePostsRequest;
import models.response.api.bb.AddPossessionResponse;
import models.response.api.bb.DeletePossessionResponse;
import models.response.api.bb.OnLoginResponse;
import models.response.api.bb.PopularPostsResponse;
import models.response.api.bb.RelevantsResponse;
import models.response.api.bb.StoreHistoriesResponse;
import models.response.api.bb.SuggestionsResponse;
import models.response.api.bb.SyncPossessionsResponse;
import models.response.api.bb.UpdatePossessionResponse;
import models.response.api.bb.UpdatePostsResponse;
import models.response.api.bb.WordListResponse;
import models.service.api.bb.BBService;
import models.service.api.bb.BBService.InvalidParameterException;
import models.setting.api.bb.BBStatusSetting;
import play.db.ebean.Transactional;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import utils.api.bb.LogUtil;

public class BB extends Controller {

	/**
	 * クライアントが掲示板にログインした時に呼ぶ、NitechUser追加/更新用のアクション
	 * @return
	 */
	@Transactional
	@BodyParser.Of(BodyParser.Json.class)
	public static Result onLogin() {
		OnLoginResponse response = null;
		
		try {
			OnLoginRequest json = Json.fromJson(request().body().asJson(), OnLoginRequest.class);
			response = BBService.use().procOnLogin(json);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new OnLoginResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new OnLoginResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}

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
	 * 掲示の情報を更新するアクション
	 * @return
	 */
	@Transactional
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updatePosts() {
		UpdatePostsResponse response = null;
		
		try {
			UpdatePostsRequest json = Json.fromJson(request().body().asJson(), UpdatePostsRequest.class);
			response = BBService.use().procUpdatePosts(json);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new UpdatePostsResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new UpdatePostsResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 新規掲示を追加するアクション
	 * @return
	 */
	@Transactional
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
	 * 掲示保持リストを更新するアクション
	 * @return
	 */
	@Transactional
	@BodyParser.Of(BodyParser.Json.class)
	public static Result updatePossessions() {
		UpdatePossessionResponse response = null;
		
		try {
			UpdatePossessionsRequest json = Json.fromJson(request().body().asJson(), UpdatePossessionsRequest.class);
			response = BBService.use().procUpdatePossessions(json);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new UpdatePossessionResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new UpdatePossessionResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 掲示を消すアクション (invalidate)
	 * @return
	 */
	@Transactional
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
	 * 掲示保持リストを同期するアクション
	 * @return
	 */
	public static Promise<Result> syncPossessions() {
		final Request httpRequest = request();
		
		return Promise.promise(new Function0<Result>() {
			@Override
			public Result apply() throws Throwable {
				SyncPossessionsResponse response = null;
				
				try {
					SyncPossessionsRequest request = Json.fromJson(httpRequest.body().asJson(), SyncPossessionsRequest.class);
					response = BBService.use().procSyncPossessions(request);
				} catch (InvalidParameterException e) {
					LogUtil.info("BB", e);
					response = new SyncPossessionsResponse(BBStatusSetting.BadRequest);
					response.setMessage(e.getLocalizedMessage());
					return badRequest(Json.toJson(response));
				} catch (Exception e) {
					LogUtil.error("BB", e);
					response = new SyncPossessionsResponse(BBStatusSetting.InternalServerError);
					response.setMessage(e.getLocalizedMessage());
					return internalServerError(Json.toJson(response));
				}
				
				return ok(Json.toJson(response));
			}
		});
	}
	
	/**
	 * 掲示閲覧履歴を追加するアクション
	 * @return
	 */
	@Transactional
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
	 * 人気掲示を返すアクション
	 * @param hashedNitechId
	 * @param threshold
	 * @param limit
	 * @return
	 */
	public static Result popularPosts(String hashedNitechId, Long threshold, Integer limit) {
		PopularPostsResponse response = null;
		
		try {
			response = BBService.use().procPopularPosts(hashedNitechId, threshold, limit);
		} catch (InvalidParameterException e) {
			LogUtil.info("BB", e);
			response = new PopularPostsResponse(BBStatusSetting.BadRequest);
			response.setMessage(e.getLocalizedMessage());
			return badRequest(Json.toJson(response));
		} catch (Exception e) {
			LogUtil.error("BB", e);
			response = new PopularPostsResponse(BBStatusSetting.InternalServerError);
			response.setMessage(e.getLocalizedMessage());
			return internalServerError(Json.toJson(response));
		}
		
		return ok(Json.toJson(response));
	}
	
	/**
	 * 関連掲示を返すアクション
	 * @return
	 */
	public static Result relevants(String idDate, int idIndex, Double threshold, Integer limit) {
		RelevantsResponse response = null;
		
		try {
			response = BBService.use().procRelevants(idDate, idIndex, threshold, limit);
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
