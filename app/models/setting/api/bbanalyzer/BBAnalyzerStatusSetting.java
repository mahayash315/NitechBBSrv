package models.setting.api.bbanalyzer;

public enum BBAnalyzerStatusSetting {

	OK			(20, "ok."),
	BAD_REQUEST	(40, "ng."),
	NO_RESULT	(50, "no result.");
	
	private Integer code;
	private String status;
	
	private BBAnalyzerStatusSetting(Integer code, String status) {
		this.code = code;
		this.status = status;
	}

	public Integer getCode() {
		return code;
	}

	public String getStatus() {
		return status;
	}

	
}
