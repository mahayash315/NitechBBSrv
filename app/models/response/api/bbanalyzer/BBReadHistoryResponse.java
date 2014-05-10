package models.response.api.bbanalyzer;

public class BBReadHistoryResponse {
	
	private int code;
	private String status;
	private String message;
	private BBAnalyzerResult result;
	

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
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
	public BBAnalyzerResult getResult() {
		return result;
	}
	public void setResult(BBAnalyzerResult result) {
		this.result = result;
	}
	
}
