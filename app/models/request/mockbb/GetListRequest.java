package models.request.mockbb;



public class GetListRequest {
	public String uri;
	
	public String order;
	public String order_kind;
	public boolean no_read = false; // true=既読非表示
	public boolean on_flag = false; // true=強調のみ表示
	public boolean reference_flag = true; // true=参考非表示
	public String search_keyword;
	
	public String id_date;
	public Integer id_index;
	public boolean checked;
}
