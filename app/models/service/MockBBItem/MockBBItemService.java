package models.service.MockBBItem;

import models.entity.MockBBItem;
import models.request.mockbb.GetListRequest;
import models.view.mockbb.GetListDto;
import models.view.mockbb.admin.ManageDto;

import com.avaje.ebean.Page;

public class MockBBItemService {

	public static MockBBItemService use() {
		return new MockBBItemService();
	}
	
	public GetListDto procGetListRequest(GetListRequest request) {
		GetListDto dto = new GetListDto();
		
		String orderByClause = null;
		String filter = null;
		if (request.order != null && request.order_kind != null) {
			orderByClause = request.order + " " + request.order_kind;
		}
		if (request.search_keyword != null) {
			filter = request.search_keyword;
		}
		
		dto.items = new MockBBItem().findList(orderByClause, filter, request.no_read, request.reference_flag, request.on_frag);
		
		return dto;
	}
	
	public ManageDto procAdminManage(Integer pageSource, String sortBy, String order, String filter) {
		ManageDto dto = new ManageDto();
		
		String orderByClause = null;
		if (sortBy != null && order != null) {
			orderByClause = sortBy + " " + order;
		}
		
		// 取得
		Page<MockBBItem> page = new MockBBItem().findPage(pageSource, orderByClause, filter, false, false, false);

		dto.currentPage = pageSource;
		dto.currentSortBy = sortBy;
		dto.currentOrder = order;
		dto.currentFilter = filter;
		dto.items = page.getList();
		dto.hasPrevPage = page.hasPrev();
		dto.hasNextPage = page.hasNext();
		
		return dto;
	}
	
}
