package controllers.api;

import models.request.api.bbanalyzer.BBNewItemHeadsRequest;
import models.request.api.bbanalyzer.BBReadHistoryRequest;
import models.response.api.bbanalyzer.BBNewItemHeadsResponse;
import models.response.api.bbanalyzer.BBReadHistoryResponse;
import models.service.BBItemHead.BBItemHeadService;
import models.service.BBReadHistory.BBReadHistoryService;
import models.service.api.bbanalyzer.BBAnalyzerService;
import models.setting.api.bbanalyzer.BBAnalyzerStatusSetting;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.api.bbanalyzer.GsonUtil;

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
		BBNewItemHeadsResponse response = null;
		
		JsonNode jsonNode = request().body().asJson();
		
		// アサート
		if( jsonNode == null ) {
			response = new BBNewItemHeadsResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error.");
			return internalServerError(Json.toJson(response));
		}
		
		try {
			// オブジェクトに変換
			String json = Json.stringify(jsonNode);
			BBNewItemHeadsRequest request = GsonUtil.use().fromJson(json, BBNewItemHeadsRequest.class);

			Ebean.beginTransaction();
			try {
				// リクエストを処理
				response = BBItemHeadService.use().storeBBNewItemHeads(request);

				// OK の場合のみ commit する
				if (response.getCode().equals(BBAnalyzerStatusSetting.OK.getCode())) {
					Ebean.commitTransaction();
				} else {
					Ebean.rollbackTransaction();
				}
			} catch (Exception e0) {
				e0.printStackTrace();
				response = new BBNewItemHeadsResponse(BBAnalyzerService.use().getInternalErrorResponse());
				response.setMessage(e0.getLocalizedMessage());
				
				Ebean.rollbackTransaction();
			} finally {
				Ebean.endTransaction();
			}
			
			
			
			// レスポンド
			return ok(GsonUtil.use().toJson(response));
		} catch (JsonSyntaxException e) {
			// JSON パースエラー
			response = new BBNewItemHeadsResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error. Invalid JSON sent.");
			return badRequest(Json.toJson(response));
		}
	}
	
	
	/**
	 * 閲覧履歴の送信を受け付ける
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	@Transactional
	public static Result postReadHistory() {
		BBReadHistoryResponse response = null;
		
		JsonNode jsonNode = request().body().asJson();
		
		// アサート
		if( jsonNode == null ) {
			response = new BBReadHistoryResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error.");
			return badRequest(Json.toJson(response));
		}
		
		try {
			// オブジェクトに変換
			String json = Json.stringify(jsonNode);
			BBReadHistoryRequest request = GsonUtil.use().fromJson(json, BBReadHistoryRequest.class);
			
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
				e0.printStackTrace();
				response = new BBReadHistoryResponse(BBAnalyzerService.use().getInternalErrorResponse());
				response.setMessage(e0.getLocalizedMessage());

				Ebean.rollbackTransaction();
			} finally {
				Ebean.endTransaction();
			}
			
			// レスポンド
			return ok(GsonUtil.use().toJson(response));
		} catch (JsonSyntaxException e) {
			// JSON パースエラー
			response = new BBReadHistoryResponse(BBAnalyzerService.use().getBadRequestResponse());
			response.setMessage("Parse error. Invalid JSON sent.");
			return badRequest(Json.toJson(response));
		}
	}
}
