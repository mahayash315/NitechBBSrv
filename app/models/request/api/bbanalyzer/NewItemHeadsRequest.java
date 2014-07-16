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
	
	
	
	
	
	
	
	
	
	
	public class BBItemHead implements Comparable<BBItemHead> {
		private String mIdDate;
		private String mIdIndex;
		private String mDateShow;
		private String mDateExec;
		private String mTitle;
		private String mAuthor;
		private boolean mIsReference = false;
		private boolean mIsRead = false;
		private boolean mIsFavorite = false;
		private boolean mIsNew = true;
		
		public BBItemHead() { }

		public BBItemHead(String idDate, String idIndex, String dateShow,
				String dateExec, String title, String author, int isReference, int isRead, int isFavorite, int isNew) {
			this(idDate, idIndex, dateShow, dateExec, title, author, (isReference != 0), (isRead != 0), (isFavorite != 0), (isNew != 0));
		}

		public BBItemHead(String idDate, String idIndex, String dateShow,
				String dateExec, String title, String author, boolean isReference,
				boolean isRead, boolean isFavorite, boolean isNew) {
			this.mIdDate = idDate;
			this.mIdIndex = idIndex;
			this.mDateShow = dateShow;
			this.mDateExec = dateExec;
			this.mTitle = title;
			this.mAuthor = author;
			this.mIsReference = isReference;
			this.mIsRead = isRead;
			this.mIsFavorite = isFavorite;
			this.mIsNew = isNew;
		}

		public void setIdDate(String idDate) { mIdDate = idDate; }
		public void setIdIndex(String idIndex) { mIdIndex = idIndex; }
		public void setDateShow(String dateShow) { mDateShow = dateShow; }
		public void setDateExec(String dateExec) { mDateExec = dateExec; }
		public void setTitle(String title) { mTitle = title; }
		public void setAuthor(String author) { mAuthor = author; }
		public void setIsReference(boolean isReference) { mIsReference = isReference; }
		public void setIsRead(boolean isRead) { mIsRead = isRead; }
		public void setIsFavorite(boolean isFavorite) { mIsFavorite = isFavorite; }
		public void setIsNew(boolean isNew) { mIsNew = isNew; }
		
		public String getIdDate() { return mIdDate; }
		public String getIdIndex() { return mIdIndex; }
		public String getDateShow() { return mDateShow; }
		public String getDateExec() { return mDateExec; }
		public String getTitle() { return mTitle; }
		public String getAuthor() { return mAuthor; }
		public boolean getIsReference() { return mIsReference; }
		public boolean getIsRead() { return mIsRead; }
		public boolean getIsFavorite() { return mIsFavorite; }
		public boolean getIsNew() { return mIsNew; }
		

		@Override
		public boolean equals(Object o) {
			BBItemHead item = (BBItemHead) o;
			return (item.getIdDate() == mIdDate && item.getIdIndex() == mIdIndex);
		}
		
		@Override
		public int compareTo(BBItemHead item) {
			return mIdDate.compareTo(item.getIdDate()) * 10 + mIdIndex.compareTo(item.getIdIndex());
		}
		
	}
}
