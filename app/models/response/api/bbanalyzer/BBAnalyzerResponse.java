package models.response.api.bbanalyzer;


public class BBAnalyzerResponse {

	protected Integer code;
	protected String status;
	protected String message;
	

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
