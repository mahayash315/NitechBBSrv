package controllers.mockbb;

import static play.data.Form.*;

import java.util.Date;

import models.entity.MockBBItem;
import models.entity.MockBBItem.MockBBItemPK;
import models.request.mockbb.admin.CreateItemRequest;
import models.service.MockBBItem.MockBBItemService;
import models.view.mockbb.admin.ManageDto;
import play.data.Form;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;

public class Admin extends Controller {
	
	public static Call DEFAULT_MANAGE_CALL = controllers.mockbb.routes.Admin.manage(1, null, null, null);

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
		// フォーム取得
		Form<CreateItemRequest> createForm = form(CreateItemRequest.class).bindFromRequest();
		
		// バリデーション
		if (createForm.hasErrors()) {
			return badRequest(views.html.mockbb.admin.createItemForm.render(createForm));
		}
		
		Ebean.beginTransaction();
		try {
			// 挿入
			MockBBItem item = MockBBItemService.use().procCreateItem(createForm.get());
			if (item != null) {
				Ebean.commitTransaction();
				return redirect(DEFAULT_MANAGE_CALL);
			} else {
				Ebean.rollbackTransaction();
				return internalServerError(views.html.mockbb.admin.createItemForm.render(createForm));
			}
		} catch (Exception e) {
			Ebean.rollbackTransaction();
			return internalServerError(e.getLocalizedMessage());
		} finally {
			Ebean.endTransaction();
		}
	}
	
	public static Result editItemForm(MockBBItemPK id) {
		return TODO;
	}
	
	public static Result edit(MockBBItemPK id) {
		return TODO;
	}
}
