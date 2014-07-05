package controllers;

import models.request.mockbb.GetDetailRequest;
import models.request.mockbb.GetListRequest;
import models.service.mockbb.MockBBItemService;
import models.view.mockbb.GetDetailDto;
import models.view.mockbb.GetListDto;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MockBB extends Controller {

	public static Result redirectToIndex() {
		return redirect(controllers.routes.MockBB.index());
	}
	
	public static Result index() {
		return redirect(controllers.routes.MockBB.getList());
	}
	
	
	public static Result getList() {
		try {
			Form<GetListRequest> requestForm = play.data.Form.form(GetListRequest.class).bindFromRequest();
			
			GetListRequest request = requestForm.get();
			
			if (request.uri != null) {
				if (request.uri.equalsIgnoreCase("readcontrol")) {
					boolean result = MockBBItemService.use().procGetListReadControl(request);
					if (result) {
						return ok(String.valueOf(request.checked));
					} else {
						return badRequest();
					}
				} else if (request.uri.equalsIgnoreCase("flagcontrol")) {
					boolean result = MockBBItemService.use().procGetListFlagControl(request);
					if (result) {
						return ok(String.valueOf(request.checked));
					} else {
						return badRequest();
					}
				}
			}
		
			GetListDto dto = MockBBItemService.use().procGetListRequest(request);
			if (dto == null) {
				return badRequest("bad request");
			}
		
			return ok(views.html.mockbb.list.render(dto));
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError("internal server error");
		}
	}
	
	
	public static Result getDetail() {
		try {
			Form<GetDetailRequest> requestForm = play.data.Form.form(GetDetailRequest.class).bindFromRequest();
		
			GetDetailDto dto = MockBBItemService.use().procGetDetailRequest(requestForm.get());
			if (dto == null) {
				return badRequest("bad request");
			}
		
			return ok(views.html.mockbb.detail.render(dto));
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError("internal server error");
		}
	}
}
