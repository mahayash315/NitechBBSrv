package models.service.api.bbanalyzer;

import models.response.api.bbanalyzer.BBAnalyzerResponse;
import models.setting.api.bbanalyzer.BBAnalyzerStatusSetting;

public class BBAnalyzerService {

	public static BBAnalyzerService use() {
		return new BBAnalyzerService();
	}
	
	/**
	 * InternalServerError レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBAnalyzerResponse getInternalErrorResponse() {
		BBAnalyzerResponse result = new BBAnalyzerResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.INTERNAL_ERROR;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
	
	/**
	 * BadRequest レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBAnalyzerResponse getBadRequestResponse() {
		BBAnalyzerResponse result = new BBAnalyzerResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.BAD_REQUEST;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
	
	/**
	 * OK レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBAnalyzerResponse getOKResponse() {
		BBAnalyzerResponse result = new BBAnalyzerResponse();
		BBAnalyzerStatusSetting status = BBAnalyzerStatusSetting.OK;
		
		result.setCode(status.getCode());
		result.setStatus(status.getStatus());
		
		return result;
	}
}
