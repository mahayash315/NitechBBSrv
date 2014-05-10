package models.service.api.bbanalyzer;

import models.response.api.bbanalyzer.BBAnalyzerResponse;
import models.response.api.bbanalyzer.BBReadHistoryResponse;
import models.setting.api.bbanalyzer.BBAnalyzerStatusSetting;

public class BBAnalyzerService {

	public static BBAnalyzerService use() {
		return new BBAnalyzerService();
	}
	
	public BBAnalyzerResponse getBadRequest() {
		BBAnalyzerResponse result = new BBAnalyzerResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.BAD_REQUEST;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
	
	/**
	 * InternalServerError レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBReadHistoryResponse getInternalErrorReadHistoryResponse() {
		BBReadHistoryResponse result = new BBReadHistoryResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.INTERNAL_ERROR;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
	
	/**
	 * BadRequest レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBReadHistoryResponse getBadRequestReadHistoryResponse() {
		BBReadHistoryResponse result = new BBReadHistoryResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.BAD_REQUEST;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
	
	/**
	 * OK レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBReadHistoryResponse getOKReadHistoryResponse() {
		BBReadHistoryResponse result = new BBReadHistoryResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.OK;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
}
