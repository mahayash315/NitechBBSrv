package models.service.bb;

import java.util.HashMap;
import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.History;
import models.entity.bb.UserCluster;
import models.entity.bb.Word;
import models.entity.bb.WordInPost;

public class AtomCluster extends Cluster {

	public UserCluster userCluster;
	
	public AtomCluster(NitechUser nitechUser) {
		// ユーザの閲覧履歴から「興味あり」クラスの
		// 条件付き確率パラメータ pi を成分とする方向ベクトルを取得
		List<History> histories = new History(nitechUser).findList(null);
		if (histories != null) {
			HashMap<Word,Double> vector = new HashMap<Word,Double>();
			List<Word> words = new Word().findList();
			for (Word word : words) {
				vector.put(word, Double.valueOf(0));
			}
			
			if (!histories.isEmpty()) {
				for (History history : histories) {
					List<WordInPost> wordsInPost = history.getPost().getWordsInPost();
					for (WordInPost wordInPost : wordsInPost) {
						Word word = wordInPost.getWord();
						vector.put(word, Double.valueOf(vector.get(word) + 1));
					}
				}
	
				double size = (double) histories.size();
				for (Word word : vector.keySet()) {
					vector.put(word, Double.valueOf(vector.get(word) / size));
				}
			}
			
			this.vector = vector;
		}
	}
}
