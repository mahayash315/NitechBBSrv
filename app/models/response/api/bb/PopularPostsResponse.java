package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Post;
import models.setting.api.bb.BBStatusSetting;

public class PopularPostsResponse extends BBResponse {
	
	public List<Entry> posts = new ArrayList<Entry>();

	public PopularPostsResponse() {
		super();
	}
	public PopularPostsResponse(BBStatusSetting val) {
		super(val);
	}
	
	public void add(Post o, long popularity){
		Entry e = new Entry();
		e.idDate = o.getIdDate();
		e.idIndex = o.getIdIndex();
		e.popularity = popularity;
		posts.add(e);
	}
	
	
	public class Entry {
		public String idDate;
		public int idIndex;
		public long popularity;
	}

}
