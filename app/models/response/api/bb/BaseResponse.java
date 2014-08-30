package models.response.api.bb;

import models.setting.api.bb.BBStatusSetting;

public class BaseResponse {

	public Integer code;
	public String status;
	public String message;
	
	public BaseResponse() {
		
	}
	public BaseResponse(BBStatusSetting val) {
		this.code = val.code;
		this.status = val.status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
