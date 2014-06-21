package models.service.bbitem;

import models.service.bbanalyzer.BBAnalyzerService;

public class BBItemService {
	
	private BBAnalyzerService bbAnalyzerService = new BBAnalyzerService();

	public static BBItemService use() {
		return new BBItemService();
	}
	
}
