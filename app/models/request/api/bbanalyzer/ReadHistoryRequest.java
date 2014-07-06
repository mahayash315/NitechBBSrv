package models.request.api.bbanalyzer;

import java.util.ArrayList;

import utils.api.bbanalyzer.DataConverter;

public class ReadHistoryRequest {
	public String hashedNitechId;
	public ArrayList<BBReadHistoryItem> histories;
	
	public ReadHistoryRequest(String nitechId, ArrayList<BBReadHistoryItem> histories) {
		this.hashedNitechId = DataConverter.getSHA512(nitechId);
		this.histories = histories;
	}
}
