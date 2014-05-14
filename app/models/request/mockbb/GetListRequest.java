package models.request.mockbb;

public class GetListRequest {
	public String order;
	public String orderKind;
	public boolean noRead; // true=既読非表示
	public boolean onFrag; // true=強調のみ表示
	public boolean referenceFlag; // true=参考非表示
	public String searchKeyword;
}
