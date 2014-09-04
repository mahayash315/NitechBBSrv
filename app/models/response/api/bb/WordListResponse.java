package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Word;
import models.setting.api.bb.BBStatusSetting;

public class WordListResponse extends BBResponse {

	public List<Entry> words = new ArrayList<Entry>();
	
	public WordListResponse() {
		super();
	}
	public WordListResponse(BBStatusSetting val) {
		super(val);
	}

	public void add(Word o) {
		Entry e = new Entry();
		e.id = o.getId();
		e.baseForm = o.getBaseForm();
		words.add(e);
	}
	
	public class Entry {
		public long id;
		public String baseForm;
	}
}
