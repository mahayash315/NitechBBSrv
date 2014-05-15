package models.service.MockBBItem;

import models.entity.MockBBItem;
import models.entity.MockBBItem.MockBBItemPK;
import models.request.mockbb.GetDetailRequest;
import models.request.mockbb.GetListRequest;
import models.request.mockbb.admin.CreateItemRequest;
import models.request.mockbb.admin.EditItemRequest;
import models.view.mockbb.GetDetailDto;
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
		
		dto.currentOrder = request.order;
		dto.currentOrderKind = request.order_kind;
		dto.noReadFlag = request.no_read;
		dto.onFlagFlag = request.on_flag;
		dto.referenceFlag = request.reference_flag;
		dto.items = new MockBBItem().findList(orderByClause, filter, request.no_read, request.reference_flag, request.on_flag);
		
		return dto;
	}
	
	public GetDetailDto procGetDetailRequest(GetDetailRequest request) {
		GetDetailDto dto = new GetDetailDto();
		
		MockBBItem item = new MockBBItem(new MockBBItemPK(request.id_date, request.id_index)).unique();
		if (item == null) {
			return null;
		}
		
		dto.item = item;
		
		return dto;
	}
	
	public boolean procGetListReadControl(GetListRequest request) {
		if (request.id_date != null && request.id_index != null) {
			MockBBItem item = new MockBBItem(new MockBBItemPK(request.id_date, request.id_index)).unique();
			if (item == null) {
				return false;
			}
			
			item.setRead(request.checked);
			
			if (item.store() != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean procGetListFlagControl(GetListRequest request) {
		if (request.id_date != null && request.id_index != null) {
			MockBBItem item = new MockBBItem(new MockBBItemPK(request.id_date, request.id_index)).unique();
			if (item == null) {
				return false;
			}
			
			item.setFlagged(request.checked);
			
			if (item.store() != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	public ManageDto procAdminManage(Integer pageSource, String sortBy, String order, String filter) {
		ManageDto dto = new ManageDto();
		
		String orderByClause = null;
		if (sortBy != null && !sortBy.isEmpty()) {
			orderByClause = sortBy;
			if (order != null && !order.isEmpty()) {
				orderByClause = orderByClause + " " + order;
			}
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
	
	public MockBBItem procCreateItem(CreateItemRequest request) {
		MockBBItem item = new MockBBItem();
		
		item.setDateShow(request.dateShow);
		item.setDateExec(request.dateExec);
		item.setAuthor(request.author);
		item.setTitle(request.title);
		item.setRead(request.isRead);
		item.setFlagged(request.isFlagged);
		item.setReference(request.isReference);
		item.setBody(request.body);
		
		return item.store();
	}
	
	public MockBBItem procEditItem(MockBBItemPK id, EditItemRequest request) {
		MockBBItem item = new MockBBItem(id).unique();
		if (item == null) {
			return null;
		}
		
		item.setDateShow(request.dateShow);
		item.setDateExec(request.dateExec);
		item.setAuthor(request.author);
		item.setTitle(request.title);
		item.setRead(request.isRead);
		item.setFlagged(request.isFlagged);
		item.setReference(request.isReference);
		item.setBody(request.body);
		
		return item.store();
	}
	
	public EditItemRequest itemToEditItemRequest(MockBBItem item) {
		if (item != null) {
			EditItemRequest request = new EditItemRequest();
			
			request.dateShow = item.getDateShow();
			request.dateExec = item.getDateExec();
			request.isRead = item.isRead();
			request.isFlagged = item.isFlagged();
			request.isReference = item.isReference();
			request.author = item.getAuthor();
			request.title = item.getTitle();
			request.body = item.getBody();
			
			return request;
		}
		return null;
	}
}
