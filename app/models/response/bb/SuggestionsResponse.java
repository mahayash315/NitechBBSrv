package models.response.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Post;
import models.setting.bb.api.BBStatusSetting;

public class SuggestionsResponse extends BaseResponse {
	
	public List<Entry> posts = new ArrayList<Entry>();

	public SuggestionsResponse() {
		super();
	}
	public SuggestionsResponse(BBStatusSetting val) {
		super(val);
	}
	
	public void add(Post o, double value){
		Entry e = new Entry();
		e.idDate = o.getIdDate();
		e.idIndex = o.getIdIndex();
		e.value = value;
		posts.add(e);
	}
	
	
	public class Entry {
		public String idDate;
		public int idIndex;
		public double value;
	}

}
