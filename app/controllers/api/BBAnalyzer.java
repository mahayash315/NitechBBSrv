package controllers.api;

import models.entity.BBItem;
import models.entity.User;
import models.request.api.bbanalyzer.NewItemHeadsRequest;
import models.request.api.bbanalyzer.ReadHistoryRequest;
import models.response.api.bbanalyzer.ClassifyItemResponse;
import models.response.api.bbanalyzer.NewItemHeadsResponse;
import models.response.api.bbanalyzer.ReadHistoryResponse;
import models.service.api.bbanalyzer.BBAnalyzerService;
import models.service.bbanalyzer.BBItemService;
import models.service.bbanalyzer.BBReadHistoryService;
import models.setting.api.bbanalyzer.BBAnalyzerStatusSetting;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.api.bbanalyzer.GsonUtil;
import utils.api.bbanalyzer.LogUtil;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonSyntaxException;

public class BBAnalyzer extends Controller {

	
	/**
	 * 新着記事の送信を受け付ける
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result postNewItemHeads() {
		NewItemHeadsResponse response = null;
		
		JsonNode jsonNode = request().body().asJson();
		
		// アサート
		if( jsonNode == null ) {
			response = new NewItemHeadsResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error.");
			return internalServerError(GsonUtil.use().toJson(response));
		}
		
		try {
			// オブジェクトに変換
			String json = Json.stringify(jsonNode);
			NewItemHeadsRequest request = GsonUtil.use().fromJson(json, NewItemHeadsRequest.class);

			Ebean.beginTransaction();
			try {
				// リクエストを処理
				response = BBItemService.use().storeBBNewItemHeads(request);

				// OK の場合のみ commit する
				if (response.getCode().equals(BBAnalyzerStatusSetting.OK.getCode())) {
					Ebean.commitTransaction();
				} else {
					Ebean.rollbackTransaction();
				}
			} catch (Exception e0) {
				Ebean.rollbackTransaction();
				
				LogUtil.error("Exception@BBAnalyzer#postNewItemHeads()", e0);
				response = new NewItemHeadsResponse(BBAnalyzerService.use().getInternalErrorResponse());
				response.setMessage(e0.getLocalizedMessage());
			} finally {
				Ebean.endTransaction();
			}
			
			
			
			// レスポンド
			return ok(GsonUtil.use().toJson(response));
		} catch (JsonSyntaxException e) {
			// JSON パースエラー
			LogUtil.error("Exception@BBAnalyzer#postNewItemHeads()", e);
			response = new NewItemHeadsResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error. Invalid JSON sent.");
			return badRequest(GsonUtil.use().toJson(response));
		}
	}
	
	
	/**
	 * 閲覧履歴の送信を受け付ける
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public static Result postReadHistory() {
		ReadHistoryResponse response = null;
		
		JsonNode jsonNode = request().body().asJson();
		
		// アサート
		if( jsonNode == null ) {
			response = new ReadHistoryResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error.");
			return badRequest(GsonUtil.use().toJson(response));
		}
		
		try {
			// オブジェクトに変換
			String json = Json.stringify(jsonNode);
			ReadHistoryRequest request = GsonUtil.use().fromJson(json, ReadHistoryRequest.class);
			
			// 閲覧履歴を保存
			Ebean.beginTransaction();
			try {
				// リクエストを処理
				response = BBReadHistoryService.use().storeReceivedHistory(request);
				
				// OK の場合のみ commit する
				if (response.getCode().equals(BBAnalyzerStatusSetting.OK.getCode())) {
					Ebean.commitTransaction();
				} else {
					Ebean.rollbackTransaction();
				}
			} catch (Exception e0) {
				Ebean.rollbackTransaction();
				
				LogUtil.error("Exception@BBAnalyzer#postNewItemHeads()", e0);
				response = new ReadHistoryResponse(BBAnalyzerService.use().getInternalErrorResponse());
				response.setMessage(e0.getLocalizedMessage());
			} finally {
				Ebean.endTransaction();
			}
			
			try {
				// レスポンド
				return ok(GsonUtil.use().toJson(response));
			} finally {
				// 後処理：計算
				User user = new User(request.hashedNitechId).unique();
				if (user != null) {
					Ebean.beginTransaction();
					try {
						//閲覧履歴からカテゴリ決定
						BBReadHistoryService.use().categorizeFromReadHistory(user);
						
						Ebean.commitTransaction();
					} catch (Exception e) {
						Ebean.rollbackTransaction();
						LogUtil.error("Exception@BBAnalyzer#postNewItemHeads()", e);
					} finally {
						Ebean.endTransaction();
					}
				}
			}
		} catch (JsonSyntaxException e) {
			// JSON パースエラー
			LogUtil.error("Exception@BBAnalyzer#postNewItemHeads()", e);
			response = new ReadHistoryResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error. Invalid JSON sent.");
			return badRequest(GsonUtil.use().toJson(response));
		}
	}
	
	
	
	public static Result classifyItem(String hashedNitechId, String idDate, String idIndex) {
		ClassifyItemResponse response = null;
		
		if (hashedNitechId != null && idDate != null && idIndex != null) {
			try {
				// ユーザ取得
				User user = new User(hashedNitechId).unique();
				if (user == null) {
					throw new Exception("User "+hashedNitechId+" not found");
				}
				
				// 掲示取得
				BBItem item = new BBItem(idDate, idIndex).unique();
				if (item == null) {
					throw new Exception("BBItem ("+idDate+","+idIndex+") not found");
				}
				
				// クラス分類
				int classNumber = models.service.bbanalyzer.BBAnalyzerService.use().classifyItem(user, item);
				
				response = new ClassifyItemResponse(BBAnalyzerService.use().getOKResponse());
				response.clazz = Integer.valueOf(classNumber);
				
				return ok(GsonUtil.use().toJson(response));
			} catch (Exception e) {
				LogUtil.error("Exception@BBAnalyzer#classifyItem()", e);
				response = new ClassifyItemResponse(BBAnalyzerService.use().getBadRequestResponse());
				response.setMessage(e.getMessage());
				return badRequest(GsonUtil.use().toJson(response));
			}
		}
		
		response = new ClassifyItemResponse(BBAnalyzerService.use().getBadRequestResponse());
		response.setMessage("invalid parameters given.");
		return badRequest(GsonUtil.use().toJson(response));
	}
}
