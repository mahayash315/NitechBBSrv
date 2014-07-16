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
	
	
	
	
	
	
	public class BBReadHistoryItem {
		private long mOpenTime;
		private String mIdDate;
		private String mIdIndex;
		private long mReadTimeLength;
		private String mReferer;
		
		public BBReadHistoryItem(long openTime, String idDate, String idIndex, String referer) {
			this.mOpenTime = openTime;
			this.mIdDate = idDate;
			this.mIdIndex = idIndex;
			this.mReadTimeLength = 0;
			this.mReferer = referer;
		}


		public long getOpenTime() {
			return mOpenTime;
		}
		public String getIdDate() {
			return mIdDate;
		}
		public String getIdIndex() {
			return mIdIndex;
		}
		public long getReadTimeLength() {
			return mReadTimeLength;
		}
		public String getReferer() {
			return mReferer;
		}


		public void setReadTime(long readTimeLength) {
			this.mReadTimeLength = readTimeLength;
		}
		
		
	}
}
