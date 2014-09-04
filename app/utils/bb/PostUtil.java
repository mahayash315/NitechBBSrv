package utils.bb;

import java.util.HashMap;
import java.util.List;

import models.entity.Configuration;
import models.entity.bb.Post;
import models.entity.bb.Word;
import models.setting.BBSetting;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class PostUtil {

	private static Tokenizer sTokenizer = Tokenizer.builder().build();
	private static HashMap<String,Long> sWordList = new HashMap<String,Long>();
	private static long sWordListLastUpdated = 0;
	
	public static PostUtil use() {
		return new PostUtil();
	}
	
	private PostUtil() {
		
	}
	
	/**
	 * 掲示の特徴量を返す
	 * @param post
	 * @return
	 */
	public HashMap<Word,Integer> getPostFeature(Post post) {
		// 現在のメモリ上の単語リストが最新か確認
		long lastModified = Configuration.getLong(BBSetting.CONFIGURATION_KEY_WORD_LIST_LAST_MODIFIED, 0L);
		if (sWordListLastUpdated < lastModified) {
			updateWordList();
		}
		
		// 単語を抽出
		if (post != null) {
			HashMap<Word,Integer> feature = new HashMap<Word,Integer>();
			HashMap<String,Integer> map = findFeatureWordsInPost(post);
			
			for (String baseForm : map.keySet()) {
				Long wid = sWordList.get(baseForm);
				if (wid != null) {
					Word word = new Word(wid).unique();
					feature.put(word, map.get(baseForm));
				}
			}
			
			// 結果を返す
			return feature;
		}
		return null;
	}
	
	/**
	 * メモリ上の単語リストを最新のものに更新する
	 */
	private void updateWordList() {
		sWordList.clear();

		sWordListLastUpdated = System.currentTimeMillis();
		
		List<Word> list = new Word().findList();
		for (Word word : list) {
			sWordList.put(word.getBaseForm(), word.getId());
		}
	}
	
	/**
	 * 掲示の掲示者、件名を形態素解析して含まれる単語の原型とその数を返す
	 * @param post
	 * @return
	 */
	public HashMap<String,Integer> findFeatureWordsInPost(Post post) {
		if (post != null) {
			HashMap<String,Integer> map = new HashMap<String,Integer>();
			
			// 投稿者を形態素解析
			List<Token> tAuthor = sTokenizer.tokenize(post.getAuthor());
			for (Token t : tAuthor) {
				String f = t.getAllFeaturesArray()[0];
				if (f.equals("名詞") || f.equals("動詞")) {
					String baseForm = t.getBaseForm();
					Integer count = map.get(baseForm);
					if (count == null) {
						count = Integer.valueOf(0);
					}
					map.put(baseForm,count+1);
				}
			}
			
			// 件名を形態素解析
			List<Token> tTitle = sTokenizer.tokenize(post.getTitle());
			for (Token t : tTitle) {
				String f = t.getAllFeaturesArray()[0];
				if (f.equals("名詞") || f.equals("動詞")) {
					String baseForm = t.getBaseForm();
					Integer count = map.get(baseForm);
					if (count == null) {
						count = Integer.valueOf(0);
					}
					map.put(baseForm,count+1);
				}
			}
			
			return map;
		}
		return null;
	}

}
