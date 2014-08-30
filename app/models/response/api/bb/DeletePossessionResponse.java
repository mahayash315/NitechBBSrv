package models.response.api.bb;

import java.util.ArrayList;
import java.util.List;

import models.entity.bb.Word;
import models.setting.api.bb.BBStatusSetting;

public class DeletePossessionResponse extends BaseResponse {

	public DeletePossessionResponse() {
		super();
	}
	public DeletePossessionResponse(BBStatusSetting val) {
		super(val);
	}

}
