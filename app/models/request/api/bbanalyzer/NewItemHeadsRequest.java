package models.request.api.bbanalyzer;

import java.util.List;

import utils.api.bbanalyzer.DataConverter;

public class NewItemHeadsRequest {
	public String hashedNitechId;
	public long generatedTime;
	public List<BBItemHead> list;
	
	public NewItemHeadsRequest(String nitechId, long generatedTime, List<BBItemHead> list) {
		this.hashedNitechId = DataConverter.getSHA512(nitechId);
		this.generatedTime = generatedTime;
		this.list = list;
	}
}
