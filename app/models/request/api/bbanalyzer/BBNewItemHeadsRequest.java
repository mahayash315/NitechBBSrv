package models.request.api.bbanalyzer;

import java.util.List;

import utils.api.bbanalyzer.DataConverter;

public class BBNewItemHeadsRequest {
	public String hashedNitechId;
	public long generatedTime;
	public List<BBItemHead> list;
	
	public BBNewItemHeadsRequest(String nitechId, long generatedTime, List<BBItemHead> list) {
		this.hashedNitechId = DataConverter.getSHA512(nitechId);
		this.generatedTime = generatedTime;
		this.list = list;
	}
}
