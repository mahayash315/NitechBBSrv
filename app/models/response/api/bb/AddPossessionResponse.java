package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Post;
import models.setting.api.bb.BBStatusSetting;

public class AddPossessionResponse extends BBResponse {
	public List<Entry> infoLackingPosts = new ArrayList<Entry>();

	public AddPossessionResponse() {
		super();
	}
	public AddPossessionResponse(BBStatusSetting val) {
		super(val);
	}
	
	public void addInfoLackingPost(Post o) {
		Entry e = new Entry();
		e.idDate = o.getIdDate();
		e.idIndex = o.getIdIndex();
		infoLackingPosts.add(e);
	}

	public class Entry {
		public String idDate;
		public int idIndex;
	}
}
