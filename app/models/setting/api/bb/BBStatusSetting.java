package models.setting.api.bb;

public enum BBStatusSetting {
	OK(20, "ok"),
	BadRequest(40, "bad request"),
	NotFound(41, "not found"),
	InternalServerError(50, "internal server error")
	;

	private BBStatusSetting(Integer code, String status) {
		this.code = code;
		this.status = status;
	}
	
	public Integer code;
	public String status;
}
