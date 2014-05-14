package controllers;

import static play.data.Form.*;
import models.request.mockbb.GetListRequest;
import models.response.mockbb.GetListResponse;
import models.service.MockBBItem.MockBBItemService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MockBB extends Controller {

	
	public static Result getList() {
		
		try {
			Form<GetListRequest> requestForm = form(GetListRequest.class).bindFromRequest();
		
			GetListResponse response = MockBBItemService.use().procGetListRequest(requestForm.get());
			if (response == null) {
				return badRequest("bad request");
			}
		
			return ok(views.html.mockbb.list.render(response));
		} catch (Exception e) {
			return internalServerError("internal server error");
		}
	}
}
