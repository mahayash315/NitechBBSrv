package models.response.api.bbanalyzer;

public class ClassifyItemResponse extends BBAnalyzerResponse {
	
	public Integer clazz;

	public ClassifyItemResponse() {
		super();
	}
	public ClassifyItemResponse(BBAnalyzerResponse base) {
		super(base);
	}
}
