package models.request.api.bbanalyzer;


public class BBReadHistoryItem {
	private long mOpenTime;
	private String mIdDate;
	private String mIdIndex;
	private long mReadTimeLength;
	
	public BBReadHistoryItem(long openTime, String idDate, String idIndex) {
		this.mOpenTime = openTime;
		this.mIdDate = idDate;
		this.mIdIndex = idIndex;
		this.mReadTimeLength = 0;
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


	public void setReadTime(long readTimeLength) {
		this.mReadTimeLength = readTimeLength;
	}
	
	
}
