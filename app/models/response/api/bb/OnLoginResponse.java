package models.response.api.bb;

import models.setting.api.bb.BBStatusSetting;

public class OnLoginResponse extends BaseResponse {
	public Long lastLogin;

	public OnLoginResponse() {
	}

	public OnLoginResponse(BBStatusSetting val) {
		super(val);
	}

}
