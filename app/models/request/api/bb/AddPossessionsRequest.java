package models.request.api.bb;

import java.util.List;

public class AddPossessionsRequest {
	public String hashedNitechId;
	public List<Entry> possessions;
	
	public class Entry {
		public String idDate;
		public int idIndex;
	}
}
