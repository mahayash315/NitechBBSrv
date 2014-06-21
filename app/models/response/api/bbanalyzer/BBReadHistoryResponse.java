package models.response.api.bbanalyzer;

public class BBReadHistoryResponse extends BBAnalyzerResponse {
	
	public BBReadHistoryResponse() {
		super();
	}
	
	public BBReadHistoryResponse(BBAnalyzerResponse base) {
		super();
		this.code = base.code;
		this.status = base.status;
		this.message = base.message;
	}
}
