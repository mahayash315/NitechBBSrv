package models.service.bbitem;

import java.util.Map;
import java.util.Set;

import models.entity.BBItem;
import models.entity.BBItemWordCount;
import models.entity.BBWord;
import models.entity.User;
import models.request.api.bbanalyzer.BBNewItemHeadsRequest;
import models.response.api.bbanalyzer.BBNewItemHeadsResponse;
import models.service.AbstractService;
import models.service.api.bbanalyzer.BBAnalyzerService;
import models.service.user.UserModelService;
import utils.api.bbanalyzer.LogUtil;
import utils.bbanalyzer.BBAnalyzerUtil;

public class BBItemService extends AbstractService {
	
	private BBAnalyzerService bbAnalyzerService = new BBAnalyzerService();

	public static BBItemService use() {
		return new BBItemService();
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
		boolean isNew = false, isChanged = false;
		// 各 BBItemHead に関して
		for (models.request.api.bbanalyzer.BBItemHead itemHead : request.list) {
			BBItem item = new BBItem(itemHead.getIdDate(), itemHead.getIdIndex()).unique();
			if (item == null) {
				isNew = true;
				item = new BBItem();
				item.setIdDate(itemHead.getIdDate());
				item.setIdIndex(itemHead.getIdIndex());
			}
			String oldDateShow = item.getDateShow();
			String oldDateExec = item.getDateExec();
			String oldAuthor = item.getAuthor();
			String oldTitle = item.getTitle();
			
			String dateShow = itemHead.getDateShow();
			String dateExec = itemHead.getDateExec();
			String author = itemHead.getAuthor();
			String title = itemHead.getTitle();
			
			if (dateShow != null && !dateShow.isEmpty()) {
				if (oldDateShow != null && !oldDateShow.equals(dateShow)) {
					isChanged = true;
					item.setDateShow(dateShow);
				}
			}
			if (dateExec != null && !dateExec.isEmpty()) {
				if (oldDateExec != null && !oldDateExec.equals(dateExec)) {
					isChanged = true;
					item.setDateExec(dateExec);
				}
			}
			if (author != null && !author.isEmpty()) {
				if (oldAuthor != null && !oldAuthor.equals(author)) {
					isChanged = true;
					item.setAuthor(author);
				}
			}
			if (title != null && !title.isEmpty()) {
				if (oldTitle != null && !oldTitle.equals(title)) {
					isChanged = true;
					item.setTitle(title);
				}
			}
			
//			// カテゴリ推定
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
			
			if (isNew || isChanged) {
				// 更新された場合は BBItemWordCount を更新
				if (!updateBBItemWordCount(item)) {
					return new BBNewItemHeadsResponse(BBAnalyzerService.use().getInternalErrorResponse());
				}
			}
		}
		LogUtil.info("ESTIMATED "+c+" ITEMS");
		
		// 成功
		BBNewItemHeadsResponse response = new BBNewItemHeadsResponse(BBAnalyzerService.use().getOKResponse());
		response.setMessage("OK");
		return response;
	}
	
	
	
	
	
	private boolean updateBBItemWordCount(BBItem item) {
		if (item != null) {
			// 既存の BBItemWordCount をすべて削除
			Set<BBItemWordCount> wordCounts = new BBItemWordCount().findSetByItem(item);
			for(BBItemWordCount wordCount : wordCounts) {
				wordCount.remove();
			}
			
			// 特徴形態素のみをカウントする
			Map<BBWord, Integer> features = BBAnalyzerUtil.use().countFeatures(item);
			for(BBWord bbWord : features.keySet()) {
				int count = features.get(bbWord).intValue();
				BBItemWordCount wordCount = new BBItemWordCount(item, bbWord);
				wordCount.setCount(count);
				if (wordCount.store() == null) {
					return false;
				}
			}
		}
		return true;
	}
}
