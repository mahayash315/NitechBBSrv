package models.response.api.bb;

import models.setting.bb.api.BBStatusSetting;

public class BaseResponse {

	private Integer code;
	private String status;
	private String message;
	
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
