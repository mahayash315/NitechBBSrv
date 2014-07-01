package models.service.bbitemhead;

import models.entity.BBItemHead;
import models.entity.User;
import models.request.api.bbanalyzer.BBNewItemHeadsRequest;
import models.response.api.bbanalyzer.BBNewItemHeadsResponse;
import models.service.AbstractService;
import models.service.api.bbanalyzer.BBAnalyzerService;
import models.service.user.UserModelService;
import utils.api.bbanalyzer.LogUtil;

public class BBItemHeadService extends AbstractService {
	
	private models.service.bbanalyzer.BBAnalyzerService bbAnalyzerService = new models.service.bbanalyzer.BBAnalyzerService();

	public static BBItemHeadService use() {
		return new BBItemHeadService();
	}
	
	
	/**
	 * BBNewItemHeadsRequest を処理する
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public BBNewItemHeadsResponse storeBBNewItemHeads(BBNewItemHeadsRequest request) throws Exception {
		
		// User 取得
		User user = UserModelService.use().findByNitechId(request.hashedNitechId);
		if (user == null) {
			// 新規 User 作成
			user = new User(request.hashedNitechId);
			
			// User の保存
			if (user.store() == null) {
				// 保存に失敗した場合は internalServerError を返す
				return new BBNewItemHeadsResponse(BBAnalyzerService.use().getInternalErrorResponse());
			}
		}
		
		int c = 0;
		// 各 BBItemHead に関して
		for (models.request.api.bbanalyzer.BBItemHead itemHead : request.list) {
			BBItemHead item = BBItemHeadModelService.use().findByUserDateIndex(user, itemHead.getIdDate(), itemHead.getIdIndex());
			if (item == null) {
				item = new BBItemHead();
				item.setUser(user);
				item.setIdDate(itemHead.getIdDate());
				item.setIdIndex(itemHead.getIdIndex());
			}
			
			String dateShow = itemHead.getDateShow();
			String dateExec = itemHead.getDateExec();
			String author = itemHead.getAuthor();
			String title = itemHead.getTitle();
			
			if (dateShow != null && !dateShow.isEmpty()) {
				item.setDateShow(dateShow);
			}
			if (dateExec != null && !dateExec.isEmpty()) {
				item.setDateExec(dateExec);
			}
			if (author != null && !author.isEmpty()) {
				item.setAuthor(author);
			}
			if (title != null && !title.isEmpty()) {
				item.setTitle(title);
			}
			
			// カテゴリ推定
//			BBCategory category = bbAnalyzerService.estimate(user, item);
//			if (category != null) {
//				++c;
//				BBItemAppendix appendix = item.getAppendix();
//				if (appendix == null) {
//					appendix = new BBItemAppendix(item, category);
//					item.setAppendix(appendix);
//				} else {
//					appendix.setCategory(category);
//				}
//				appendix.store();
//			}
			
			if (item.store() == null) {
				// 保存に失敗した場合は internalServerError を返す
				return new BBNewItemHeadsResponse(BBAnalyzerService.use().getInternalErrorResponse());
			}
		}
		LogUtil.info("ESTIMATED "+c+" ITEMS");
		
		// 成功
		BBNewItemHeadsResponse response = new BBNewItemHeadsResponse(BBAnalyzerService.use().getOKResponse());
		response.setMessage("OK");
		return response;
	}
}
