package models.service.BBReadHistory;

import models.entity.BBItemHead;
import models.entity.BBReadHistory;
import models.entity.User;
import models.request.api.bbanalyzer.BBReadHistoryItem;
import models.request.api.bbanalyzer.BBReadHistoryRequest;
import models.response.api.bbanalyzer.BBReadHistoryResponse;
import models.service.BBItemHead.BBItemHeadModelService;
import models.service.User.UserModelService;
import models.service.api.bbanalyzer.BBAnalyzerService;

public class BBReadHistoryService {

	public static BBReadHistoryService use() {
		return new BBReadHistoryService();
	}
	
	
	/**
	 * BBReadHistoryRequest を処理する
	 * @param request
	 * @return
	 */
	public BBReadHistoryResponse storeReceivedHistory(BBReadHistoryRequest request) {
		
		// User 取得
		User user = UserModelService.use().findByNitechId(request.hashedNitechId);
		if (user == null) {
			// 新規 User 作成
			user = new User(request.hashedNitechId);
			
			// User の保存
			if (user.store() == null) {
				// 保存に失敗した場合は internalServerError を返す
				return BBAnalyzerService.use().getInternalErrorReadHistoryResponse();
			}
		}
		
		
		// ReadHistory 保存
		for (BBReadHistoryItem item : request.histories) {
			// BBItemHead 取得
			BBItemHead head = BBItemHeadModelService.use().findByUserDateIndex(user, item.getIdDate(), item.getIdIndex());
			if (head == null) {
				// 新規 BBItemHead 作成
				head = new BBItemHead(item.getIdDate(), item.getIdIndex(), null, null, null);
				
				// BBItemHead の保存
				if (head.store() == null) {
					// 保存に失敗した場合は internalServerError を返す
					return BBAnalyzerService.use().getInternalErrorReadHistoryResponse();
				}
			}
			
			// TODO 作る
			BBReadHistory history = new BBReadHistory();
		}
		
		
		// 成功
		BBReadHistoryResponse response = BBAnalyzerService.use().getOKReadHistoryResponse();
		response.setMessage("OK");
		return response;
	}
}
