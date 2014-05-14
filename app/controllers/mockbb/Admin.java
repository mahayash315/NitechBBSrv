package controllers.mockbb;

import static play.data.Form.*;

import java.util.Date;

import models.entity.MockBBItem.MockBBItemPK;
import models.request.mockbb.admin.CreateItemRequest;
import models.service.MockBBItem.MockBBItemService;
import models.view.mockbb.admin.ManageDto;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Admin extends Controller {

	public static Result index() {
		return TODO;
	}
	
	public static Result manage(Integer pageSource, String sortBy, String order, String filter) {
		
		try {
			// 取得
			ManageDto dto = MockBBItemService.use().procAdminManage(pageSource, sortBy, order, filter);
			
			// 表示
			if (dto != null) {
				return ok(views.html.mockbb.admin.manage.render(dto));
			} else {
				return badRequest();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError(e.getLocalizedMessage());
		}
	}
	
	public static Result createItemForm() {
		
		try {
			// デフォルト値を作成
			CreateItemRequest request = new CreateItemRequest();
			request.dateShow = new Date();
			request.isReference = false;
			
			// フォーム作成
			Form<CreateItemRequest> createForm = form(CreateItemRequest.class).fill(request);
			
			// 表示
			return ok(views.html.mockbb.admin.createItemForm.render(createForm));
		} catch (Exception e) {
			e.printStackTrace();
			return internalServerError(e.getLocalizedMessage());
		}
	}
	
	public static Result create() {
		return TODO;
	}
	
	public static Result editItemForm(MockBBItemPK id) {
		return TODO;
	}
	
	public static Result edit(MockBBItemPK id) {
		return TODO;
	}
}
