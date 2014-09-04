package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Estimation;
import models.setting.api.bb.BBStatusSetting;

public class SuggestionsResponse extends BBResponse {
	
	public List<Entry> posts = new ArrayList<Entry>();

	public SuggestionsResponse() {
		super();
	}
	public SuggestionsResponse(BBStatusSetting val) {
		super(val);
	}
	
	public void add(Estimation o){
		Entry e = new Entry();
		e.idDate = o.getPost().getIdDate();
		e.idIndex = o.getPost().getIdIndex();
		e.value = o.getLiklihood();
		posts.add(e);
	}
	
	
	public class Entry {
		public String idDate;
		public int idIndex;
		public double value;
	}

}
