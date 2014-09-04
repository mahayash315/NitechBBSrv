package models.response.api.bb;

import models.setting.api.bb.BBStatusSetting;

public class BBResponse {

	public Integer code;
	public String status;
	public String message;
	
	public BBResponse() {
		
	}
	public BBResponse(BBStatusSetting val) {
		this.code = val.code;
		this.status = val.status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
