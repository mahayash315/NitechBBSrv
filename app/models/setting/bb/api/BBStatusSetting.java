package models.setting.bb.api;

public enum BBStatusSetting {
	OK(20, "ok"),
	NotFound(40, "not found"),
	BadRequest(41, "bad request"),
	InternalServerError(50, "internal server error")
	;

	private BBStatusSetting(Integer code, String status) {
		this.code = code;
		this.status = status;
	}
	
	public Integer code;
	public String status;
}
