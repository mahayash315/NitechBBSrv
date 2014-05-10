package models.service.api.bbanalyzer;

import models.response.api.bbanalyzer.BBAnalyzerResponse;
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
}
