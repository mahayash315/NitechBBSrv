package models.service.api.bb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import models.entity.Configuration;
import models.entity.NitechUser;
import models.entity.bb.Estimation;
import models.entity.bb.History;
import models.entity.bb.Possession;
import models.entity.bb.Post;
import models.entity.bb.Word;
import models.request.api.bb.AddPossessionsRequest;
import models.request.api.bb.OnLoginRequest;
import models.request.api.bb.StoreHistoriesRequest;
import models.request.api.bb.UpdatePossessionsRequest;
import models.request.api.bb.UpdatePostsRequest;
import models.response.api.bb.AddPossessionResponse;
import models.response.api.bb.DeletePossessionResponse;
import models.response.api.bb.OnLoginResponse;
import models.response.api.bb.RelevantsResponse;
import models.response.api.bb.StoreHistoriesResponse;
import models.response.api.bb.SuggestionsResponse;
import models.response.api.bb.UpdatePossessionResponse;
import models.response.api.bb.UpdatePostsResponse;
import models.response.api.bb.WordListResponse;
import models.setting.BBSetting;
import models.setting.api.bb.BBStatusSetting;
import utils.bb.PostUtil;

public class BBService {
	// TODO API から利用するサービスを記述
	
	public static BBService use() {
		return new BBService();
	}
	
	/**
	 * クライアントログイン時に呼ばれる
	 * @return
	 * @throws Exception
	 */
	public OnLoginResponse procOnLogin(OnLoginRequest request) throws Exception {
		OnLoginResponse response = null;
		
		if (request.nitechId == null) {
			throw new InvalidParameterException("null nitech id given");
		}
		
		NitechUser nitechUser = new NitechUser(request.nitechId).uniqueOrStore();
		Long lastLogin = nitechUser.getLastLogin();
		nitechUser.setLastLogin(System.currentTimeMillis());
		nitechUser.store();
		
		response = new OnLoginResponse(BBStatusSetting.OK);
		response.lastLogin = lastLogin;
		
		return response;
	}
	
	/**
	 * 単語リストを返す
	 * @return
	 */
	public WordListResponse procWordList() throws Exception {
		WordListResponse response = new WordListResponse(BBStatusSetting.OK);
		
		List<Word> list = new Word().findList();
		for (Word word : list) {
			response.add(word);
		}
		
		return response;
	}
	
	/**
	 * 掲示の情報を更新する
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public UpdatePostsResponse procUpdatePosts(UpdatePostsRequest request) throws Exception {
		if (request == null || request.posts == null) {
			throw new InvalidParameterException("null request or post list");
		}
		
		UpdatePostsResponse response = new UpdatePostsResponse(BBStatusSetting.OK);
		
		boolean isWordListUpdated = false;
		for (UpdatePostsRequest.PostEntry p : request.posts) {
			Post post = new Post(p.idDate, p.idIndex).uniqueOrStore();
			post.setAuthor(p.author);
			post.setTitle(p.title);
			post.save();
			
			// 単語リストに含まれない特徴語があれば単語リストに追加
			HashMap<String,Integer> map = PostUtil.use().findFeatureWordsInPost(post);
			for (String baseForm : map.keySet()) {
				if (baseForm != null && !baseForm.isEmpty()) {
					Word word = new Word(baseForm);
					if (word.unique() == null) {
						word = word.store();
						isWordListUpdated = true;
					}
				}
			}
		}
		if (isWordListUpdated) {
			// 単語リスト最終更新日時を更新
			Configuration.putLong(BBSetting.CONFIGURATION_KEY_WORD_LIST_LAST_MODIFIED, System.currentTimeMillis());
		}
		
		return response;
	}
	
	/**
	 * 掲示保持リストにエントリを追加する
	 * @return
	 */
	public AddPossessionResponse procAddPossessions(AddPossessionsRequest request) throws Exception {
		if (request.nitechId == null) {
			throw new InvalidParameterException("null nitech id");
		}

		NitechUser nitechUser = new NitechUser(request.nitechId).unique();
		if (nitechUser == null) {
			throw new InvalidParameterException("invalid nitech user id");
		}
		
		AddPossessionResponse response = new AddPossessionResponse(BBStatusSetting.OK);
		for (AddPossessionsRequest.Entry e : request.posts) {
			Post post = new Post(e.idDate, e.idIndex).uniqueOrStore();
			Possession possession = new Possession(nitechUser, post).uniqueOrStore();
			possession.setIsFavorite(e.isFavorite);
			possession.save();
			if (post.getAuthor() == null || post.getAuthor().isEmpty() || post.getTitle() == null || post.getTitle().isEmpty()) {
				response.addInfoLackingPost(post);
			}
		}
		
		return response;
	}
	
	/**
	 * 掲示保持リストの更新リクエストを処理する
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public UpdatePossessionResponse procUpdatePossessions(UpdatePossessionsRequest request) throws Exception {
		if (request.nitechId == null) {
			throw new InvalidParameterException("null nitech id");
		}

		NitechUser nitechUser = new NitechUser(request.nitechId).unique();
		if (nitechUser == null) {
			throw new InvalidParameterException("invalid nitech user id");
		}
		
		UpdatePossessionResponse response = new UpdatePossessionResponse(BBStatusSetting.OK);
		for (UpdatePossessionsRequest.Entry e : request.posts) {
			Post post = new Post(e.idDate, e.idIndex).unique();
			if (post != null) {
				Possession possession = new Possession(nitechUser, post);
				if (possession != null) {
					possession.setIsFavorite(e.isFavorite);
					possession.save();
				}
			}
		}
		
		return response;
	}
	
	/**
	 * 掲示保持リストからエントリを削除する
	 * @param hashedNitechId
	 * @param _idDates
	 * @param _idIndexes
	 * @return
	 */
	public DeletePossessionResponse procDeletePossessions(String hashedNitechId, String _idDates, String _idIndexes) throws Exception {
		String[] idDates = _idDates.split(",");
		String[] idIndexes = _idIndexes.split(",");
		NitechUser nitechUser = new NitechUser(hashedNitechId).unique();
		if (nitechUser == null) {
			throw new InvalidParameterException("idvalid nitech user id");
		} else if (idDates.length != idIndexes.length) {
			throw new InvalidParameterException("idvalid parameters: parameter sizes does not match");
		}

		DeletePossessionResponse response = new DeletePossessionResponse(BBStatusSetting.OK);
		for (int i = 0; i < idDates.length; ++i) {
			String idDate = idDates[i];
			int idIndex = Integer.parseInt(idIndexes[i]);
			Post post = new Post(idDate,idIndex).unique();
			if (post != null) {
				Possession possession = new Possession(nitechUser, post).unique();
				if (possession != null) {
					possession.delete();
				}
			}
		}
		
		return response;
	}
	
	/**
	 * 掲示閲覧履歴にエントリを追加する
	 * @return
	 */
	public StoreHistoriesResponse procStoreHistories(StoreHistoriesRequest request) throws Exception {
		if (request.nitechId == null) {
			throw new InvalidParameterException("null nitech id");
		}
		
		NitechUser nitechUser = new NitechUser(request.nitechId).unique();
		if (nitechUser == null) {
			throw new InvalidParameterException("invalid nitech user id");
		}
		
		StoreHistoriesResponse response = new StoreHistoriesResponse(BBStatusSetting.OK);
		for (StoreHistoriesRequest.Entry e : request.histories) {
			Post post = new Post(e.idDate, e.idIndex).unique();
			if (post != null) {
				new History(nitechUser, post, e.timestamp).store();
			}
		}
		
		return response;
	}
	
	/**
	 * nitechUser に対するおすすめ掲示を返す
	 * @param hashedNitechId
	 * @return
	 */
	public SuggestionsResponse procSuggestions(String hashedNitechId) throws Exception {
		if (hashedNitechId == null) {
			throw new InvalidParameterException("null nitechId given");
		}
		
		NitechUser nitechUser = new NitechUser(hashedNitechId).unique();
		if (nitechUser == null) {
			throw new InvalidParameterException("invalid nitech user id");
		}
		
		SuggestionsResponse response = new SuggestionsResponse(BBStatusSetting.OK);
		List<Estimation> suggestions = new Estimation(nitechUser).findSuggestions();
		for (Estimation estimation : suggestions) {
			response.add(estimation);
		}
		
		return response;
	}
	
	/**
	 * 関連掲示を返す
	 * @param hashedNitechId
	 * @return
	 */
	public RelevantsResponse procRelevants(String idDate, int idIndex, Double threshold, Integer limit) throws Exception {
		Post post = new Post(idDate,idIndex).unique();
		
		if (post == null) {
			throw new InvalidParameterException("invalid parameters given: found no such post");
		}
		
		RelevantsResponse response = new RelevantsResponse(BBStatusSetting.OK);
		Map<Post, Double> relevants = post.findRelevants(threshold, limit);
		Set<Entry<Post,Double>> entrySet = relevants.entrySet();
		for (Entry<Post,Double> entry : entrySet) {
			response.add(entry.getKey(), entry.getValue());
		}
		
		return response;
	}
	
	
	public class InvalidParameterException extends Exception {
		public InvalidParameterException() {
			super();
		}
		public InvalidParameterException(String detailMessage,
				Throwable throwable) {
			super(detailMessage, throwable);
		}
		public InvalidParameterException(String detailMessage) {
			super(detailMessage);
		}
		public InvalidParameterException(Throwable throwable) {
			super(throwable);
		}
	}
}
