package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Word;
import models.setting.api.bb.BBStatusSetting;

public class StoreHistoriesResponse extends BBResponse {

	public StoreHistoriesResponse() {
		super();
	}
	public StoreHistoriesResponse(BBStatusSetting val) {
		super(val);
	}

}
