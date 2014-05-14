package models.request.mockbb;



public class GetListRequest {
	public String order;
	public String order_kind;
	public boolean no_read; // true=既読非表示
	public boolean on_frag; // true=強調のみ表示
	public boolean reference_flag; // true=参考非表示
	public String search_keyword;
}
