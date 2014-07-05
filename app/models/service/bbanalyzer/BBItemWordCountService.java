package models.service.bbanalyzer;

import java.util.Map;
import java.util.Set;

import models.entity.BBItem;
import models.entity.BBItemWordCount;
import models.entity.BBWord;
import models.service.AbstractService;
import utils.bbanalyzer.BBAnalyzerUtil;

public class BBItemWordCountService extends AbstractService {

	public static BBItemWordCountService use() {
		return new BBItemWordCountService();
	}
	
	public boolean updateBBItemWordCount(BBItem item) {
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
