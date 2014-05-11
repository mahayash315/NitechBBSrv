package models.service.BBItemHead;

import models.entity.BBItemHead;
import models.entity.User;
import models.request.api.bbanalyzer.BBNewItemHeadsRequest;
import models.response.api.bbanalyzer.BBNewItemHeadsResponse;
import models.service.User.UserModelService;
import models.service.api.bbanalyzer.BBAnalyzerService;

public class BBItemHeadService {

	public static BBItemHeadService use() {
		return new BBItemHeadService();
	}
	
	
	/**
	 * BBNewItemHeadsRequest を処理する
	 * @param request
	 * @return
	 */
	public BBNewItemHeadsResponse storeBBNewItemHeads(BBNewItemHeadsRequest request) {
		
		// User 取得
		User user = UserModelService.use().findByNitechId(request.hashedNitechId);
		if (user == null) {
			// 新規 User 作成
			user = new User(request.hashedNitechId);
			
			// User の保存
			if (user.store() == null) {
				// 保存に失敗した場合は internalServerError を返す
				return (BBNewItemHeadsResponse) BBAnalyzerService.use().getInternalErrorResponse();
			}
		}
		
		// 各 BBItemHead に関して
		for (models.request.api.bbanalyzer.BBItemHead itemHead : request.list) {
			BBItemHead item = new BBItemHead();
			
			item.setUser(user);
			item.setIdDate(itemHead.getIdDate());
			item.setIdIndex(itemHead.getIdIndex());
			item.setDateShow(itemHead.getDateShow());
			item.setDateExec(itemHead.getDateExec());
			item.setAuthor(itemHead.getAuthor());
			item.setTitle(itemHead.getTitle());
			
			if (item.store() == null) {
				// 保存に失敗した場合は internalServerError を返す
				return (BBNewItemHeadsResponse) BBAnalyzerService.use().getInternalErrorResponse();
			}
		}
		
		return null;
	}
}
