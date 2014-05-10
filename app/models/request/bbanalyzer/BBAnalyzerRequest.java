package models.request.bbanalyzer;

import play.data.validation.Constraints;

public class BBAnalyzerRequest {

	@Constraints.Required
	private String body;
	
	

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	
}
