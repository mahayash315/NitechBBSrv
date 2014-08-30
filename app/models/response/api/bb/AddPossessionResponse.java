package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Post;
import models.setting.api.bb.BBStatusSetting;

public class AddPossessionResponse extends BaseResponse {
	public List<Entry> featureLackingPosts = new ArrayList<Entry>();

	public AddPossessionResponse() {
		super();
	}
	public AddPossessionResponse(BBStatusSetting val) {
		super(val);
	}
	
	public void addFeatureLackingPost(Post o) {
		Entry e = new Entry();
		e.idDate = o.getIdDate();
		e.idIndex = o.getIdIndex();
		featureLackingPosts.add(e);
	}

	public class Entry {
		public String idDate;
		public int idIndex;
	}
}
