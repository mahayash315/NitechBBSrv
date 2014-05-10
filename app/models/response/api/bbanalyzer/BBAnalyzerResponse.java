package models.response.api.bbanalyzer;


public class BBAnalyzerResponse {

	private Integer code;
	private String status;
	private String message;
	private BBAnalyzerResult result;
//	private String jsonData;
	
	

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
	public BBAnalyzerResult getResult() {
		return result;
	}
	public void setResult(BBAnalyzerResult result) {
		this.result = result;
	}
//	public String getJsonData() {
//		return jsonData;
//	}
//	public void setJsonData(String jsonData) {
//		this.jsonData = jsonData;
//	}
}
