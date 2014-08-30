package models.request.api.bb;

import java.util.List;

public class StoreHistoriesRequest {
	public String nitechId;
	public List<Entry> posts;
	
	public class Entry {
		public String idDate;
		public int idIndex;
		public long timestamp;
	}
}
