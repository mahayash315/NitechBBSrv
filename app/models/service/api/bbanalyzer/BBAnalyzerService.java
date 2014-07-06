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
		return new BBAnalyzerResponse(BBAnalyzerStatusSetting.INTERNAL_ERROR);
	}
	
	/**
	 * BadRequest レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBAnalyzerResponse getBadRequestResponse() {
		return new BBAnalyzerResponse(BBAnalyzerStatusSetting.BAD_REQUEST);
	}
	
	/**
	 * OK レスポンスを生成する。code と status が設定された状態を返す。
	 * @return
	 */
	public BBAnalyzerResponse getOKResponse() {
		return new BBAnalyzerResponse(BBAnalyzerStatusSetting.OK);
	}
}
