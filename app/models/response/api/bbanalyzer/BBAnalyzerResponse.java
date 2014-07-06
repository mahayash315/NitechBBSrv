package models.response.api.bbanalyzer;

import models.setting.api.bbanalyzer.BBAnalyzerStatusSetting;


public class BBAnalyzerResponse {

	protected Integer code;
	protected String status;
	protected String message;
	
	public BBAnalyzerResponse() {
		this(BBAnalyzerStatusSetting.DEFAULT);
	}
	public BBAnalyzerResponse(BBAnalyzerStatusSetting status) {
		this.code = status.getCode();
		this.status = status.getStatus();
	}
	public BBAnalyzerResponse(BBAnalyzerResponse base) {
		this.code = base.getCode();
		this.status = base.getStatus();
		this.message = base.getMessage();
	}

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
