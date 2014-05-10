package controllers.api;

import models.request.api.bbanalyzer.BBNewItemHeadsRequest;
import models.request.api.bbanalyzer.BBReadHistoryRequest;
import models.response.api.bbanalyzer.BBAnalyzerResponse;
import models.response.api.bbanalyzer.BBAnalyzerResult;
import models.service.api.bbanalyzer.BBAnalyzerService;
import models.setting.api.bbanalyzer.BBAnalyzerStatusSetting;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.api.bbanalyzer.BBAnalyzerUtil;
import utils.api.bbanalyzer.GsonUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BBAnalyzer extends Controller {

	@BodyParser.Of(BodyParser.Json.class)
	public static Result analyzeBody() {
		JsonNode json = request().body().asJson();
		
		// アサート
		if( json == null ) {
			BBAnalyzerResponse response = BBAnalyzerService.use().getBadRequest();
			response.setMessage("Parse error.");
			return badRequest(Json.toJson(response));
		}
		
		// 本文受け取り
		JsonNode jnBody = json.findPath("body");
		if( jnBody == null ) {
			BBAnalyzerResponse response = BBAnalyzerService.use().getBadRequest();
			response.setMessage("no body sent.");
			return badRequest(Json.toJson(response));
		}
		String body = jnBody.textValue();
		
		// 処理
		BBAnalyzerResult result = BBAnalyzerUtil.analyzeText(body);
		BBAnalyzerResponse response = new BBAnalyzerResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.OK;
        Gson gson = new Gson();
		
		response.setCode(status.getCode());
		response.setStatus(status.getStatus());
		response.setMessage("done");
		response.setResult(result);
		
//		if( result.getSurfaces() != null && result.getFeatures() != null ) {
//			int pos = 0, prevPos = 0;
//			int start = 0, end = 0;
//			String surface = null, feature = null;
//			SpannableStringBuilder ssb = new SpannableStringBuilder();
//			
//	        for(int i = 0; i < result.getSurfaces().size(); ++i) {
//	        	surface = result.getSurfaces().get(i);
//	        	feature = result.getFeatures().get(i);
//	            //SpannableString spannable = SanmokuDataFormatter.format("<"+e.surface+"> "+e.feature);
//	        	Logger.info("["+surface+"] : "+feature);
//	
//	    		pos = body.indexOf(surface, prevPos);
//	    		ssb.append(body.substring(prevPos, pos));
//	
//	    		start = ssb.length();
//	    		ssb.append(surface);
//	    		end = ssb.length();
//	    		
//	        	if( feature.contains("人名") ) {
//	        		ssb.setSpan(new ForegroundColorSpan(Color.RED), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//	        	} else if( feature.contains("固有名詞") ) {
//	        		ssb.setSpan(new ForegroundColorSpan(Color.MAGENTA), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//	        	} else if( feature.contains("名詞") ) {
//	        		ssb.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//	        	}
//	        	
//	        	prevPos = pos + surface.length();
//	        }
//	        
//	        response.setJsonData(gson.toJson(ssb));
//		}
		
		return ok(Json.toJson(response));
	}
	
	
	/**
	 * 新着記事の送信を受け付ける
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result postNewItemHeads() {
		JsonNode jsonNode = request().body().asJson();
		
		// アサート
		if( jsonNode == null ) {
			BBAnalyzerResponse response = BBAnalyzerService.use().getBadRequest();
			response.setMessage("Parse error.");
			return internalServerError(Json.toJson(response));
		}
		
		try {
			// オブジェクトに変換
			String json = Json.stringify(jsonNode);
			BBNewItemHeadsRequest request = GsonUtil.use().fromJson(json, BBNewItemHeadsRequest.class);
			
			
			
			
		} catch (JsonSyntaxException e) {
			// JSON パースエラー
			BBAnalyzerResponse response = BBAnalyzerService.use().getBadRequest();
			response.setMessage("Parse error. Invalid JSON sent.");
			return badRequest(Json.toJson(response));
		}
		
		return ok();
	}
	
	
	/**
	 * 閲覧履歴の送信を受け付ける
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result postReadHistory() {
		JsonNode jsonNode = request().body().asJson();
		
		// アサート
		if( jsonNode == null ) {
			BBAnalyzerResponse response = BBAnalyzerService.use().getBadRequest();
			response.setMessage("Parse error.");
			return badRequest(Json.toJson(response));
		}
		
		try {
			// オブジェクトに変換
			String json = Json.stringify(jsonNode);
			BBReadHistoryRequest request = GsonUtil.use().fromJson(json, BBReadHistoryRequest.class);
			
			
			
			
		} catch (JsonSyntaxException e) {
			// JSON パースエラー
			BBAnalyzerResponse response = BBAnalyzerService.use().getBadRequest();
			response.setMessage("Parse error. Invalid JSON sent.");
			return badRequest(Json.toJson(response));
		}
		
		return ok();
	}
}
