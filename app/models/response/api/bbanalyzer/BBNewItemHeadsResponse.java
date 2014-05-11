package models.response.api.bbanalyzer;

public class BBNewItemHeadsResponse extends BBAnalyzerResponse {

	public BBNewItemHeadsResponse() {
		super();
	}
	
	public BBNewItemHeadsResponse(BBAnalyzerResponse base) {
		super();
		this.code = base.code;
		this.status = base.status;
		this.message = base.message;
	}
}
