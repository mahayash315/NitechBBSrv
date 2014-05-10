package models.request.api.bbanalyzer;

import java.util.ArrayList;

import utils.api.bbanalyzer.DataConverter;

public class BBReadHistoryRequest {
	public String hashedNitechId;
	public ArrayList<BBReadHistoryItem> history;
	
	public BBReadHistoryRequest(String nitechId, ArrayList<BBReadHistoryItem> history) {
		this.hashedNitechId = DataConverter.getSHA512(nitechId);
		this.history = history;
	}
}
