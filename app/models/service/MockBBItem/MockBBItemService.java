package models.service.MockBBItem;

import models.entity.MockBBItem;
import models.request.mockbb.GetListRequest;
import models.response.mockbb.GetListResponse;

public class MockBBItemService {

	public static MockBBItemService use() {
		return new MockBBItemService();
	}
	
	public GetListResponse procGetListRequest(GetListRequest request) {
		GetListResponse response = new GetListResponse();
		
		String orderByClause = request.order + " " + request.orderKind;
		String filter = request.searchKeyword;
		
		response.items = new MockBBItem().findList(orderByClause, filter, request.noRead, request.referenceFlag, request.onFrag);
		
		return response;
	}
	
}
