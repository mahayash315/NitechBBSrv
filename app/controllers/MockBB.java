package controllers;

import static play.data.Form.*;
import models.request.mockbb.GetListRequest;
import models.service.MockBBItem.MockBBItemService;
import models.view.mockbb.GetListDto;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class MockBB extends Controller {

	
	public static Result index() {
		return redirect(controllers.routes.MockBB.getList());
	}
	
	
	public static Result getList() {
		try {
			Form<GetListRequest> requestForm = form(GetListRequest.class).bindFromRequest();
		
			GetListDto dto = MockBBItemService.use().procGetListRequest(requestForm.get());
			if (dto == null) {
				return badRequest("bad request");
			}
		
			return ok(views.html.mockbb.list.render(dto));
		} catch (Exception e) {
			return internalServerError("internal server error");
		}
	}
}
