package models.request.api.bbanalyzer;

import java.util.ArrayList;

import utils.api.bbanalyzer.DataConverter;

public class BBReadHistoryRequest {
	public String hashedNitechId;
	public ArrayList<BBReadHistoryItem> histories;
	
	public BBReadHistoryRequest(String nitechId, ArrayList<BBReadHistoryItem> histories) {
		this.hashedNitechId = DataConverter.getSHA512(nitechId);
		this.histories = histories;
	}
}
