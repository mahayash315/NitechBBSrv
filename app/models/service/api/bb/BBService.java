package models.service.api.bb;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import models.entity.NitechUser;
import models.entity.bb.Estimation;
import models.entity.bb.History;
import models.entity.bb.Possession;
import models.entity.bb.Post;
import models.entity.bb.Word;
import models.request.api.bb.AddPossessionsRequest;
import models.request.api.bb.StoreHistoriesRequest;
import models.response.api.bb.AddPossessionResponse;
import models.response.api.bb.DeletePossessionResponse;
import models.response.api.bb.RelevantsResponse;
import models.response.api.bb.StoreHistoriesResponse;
import models.response.api.bb.SuggestionsResponse;
import models.response.api.bb.WordListResponse;
import models.setting.api.bb.BBStatusSetting;

public class BBService {
	// TODO API から利用するサービスを記述
	
	public static BBService use() {
		return new BBService();
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
	 * 掲示保持リストにエントリを追加する
	 * @return
	 */
	public AddPossessionResponse procAddPossessions(AddPossessionsRequest request) throws Exception {
		AddPossessionResponse response = null;
		
		if (request.nitechId == null) {
			throw new InvalidParameterException("null nitech id");
		} else {
			NitechUser nitechUser = new NitechUser(request.nitechId).unique();
			if (nitechUser == null) {
				throw new InvalidParameterException("invalid nitech user id");
			} else {
				response = new AddPossessionResponse(BBStatusSetting.OK);
				for (AddPossessionsRequest.Entry e : request.possessions) {
					Post post = new Post(e.idDate, e.idIndex).uniqueOrStore();
					Possession possession = new Possession(nitechUser, post).uniqueOrStore();
					if (post.findWordsInPost().isEmpty()) {
						response.addFeatureLackingPost(post);
					}
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
		DeletePossessionResponse response = null;
		
		String[] idDates = _idDates.split(",");
		String[] idIndexes = _idIndexes.split(",");
		NitechUser nitechUser = new NitechUser(hashedNitechId).unique();
		if (nitechUser == null) {
			throw new InvalidParameterException("idvalid nitech user id");
		} else if (idDates.length != idIndexes.length) {
			throw new InvalidParameterException("idvalid parameters: parameter sizes does not match");
		} else {
			response = new DeletePossessionResponse(BBStatusSetting.OK);
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
		}
		
		return response;
	}
	
	/**
	 * 掲示閲覧履歴にエントリを追加する
	 * @return
	 */
	public StoreHistoriesResponse procStoreHistories(StoreHistoriesRequest request) throws Exception {
		StoreHistoriesResponse response = null;
		
		if (request.nitechId == null) {
			throw new InvalidParameterException("null nitech id");
		} else {
			NitechUser nitechUser = new NitechUser(request.nitechId).unique();
			if (nitechUser == null) {
				throw new InvalidParameterException("invalid nitech user id");
			} else {
				response = new StoreHistoriesResponse(BBStatusSetting.OK);
				for (StoreHistoriesRequest.Entry e : request.histories) {
					Post post = new Post(e.idDate, e.idIndex).unique();
					if (post != null) {
						new History(nitechUser, post, e.timestamp).store();
					}
				}
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
		SuggestionsResponse response = null;
		
		if (hashedNitechId == null) {
			throw new InvalidParameterException("null nitechId given");
		} else {
			NitechUser nitechUser = new NitechUser(hashedNitechId).unique();
			if (nitechUser == null) {
				throw new InvalidParameterException("invalid nitech user id");
			} else {
				response = new SuggestionsResponse(BBStatusSetting.OK);
				List<Estimation> suggestions = new Estimation(nitechUser).findSuggestions();
				for (Estimation estimation : suggestions) {
					response.add(estimation);
				}
			}
		}
		
		return response;
	}
	
	/**
	 * 関連掲示を返す
	 * @param hashedNitechId
	 * @return
	 */
	public RelevantsResponse procRelevants(String idDate, int idIndex) throws Exception {
		RelevantsResponse response = null;
		
		Post post = new Post(idDate,idIndex).unique();
		
		if (post == null) {
			throw new InvalidParameterException("invalid parameters given: found no such post");
		} else {
			response = new RelevantsResponse(BBStatusSetting.OK);
			Map<Post, Double> relevants = post.findRelevants();
			Set<Entry<Post,Double>> entrySet = relevants.entrySet();
			for (Entry<Post,Double> entry : entrySet) {
				response.add(entry.getKey(), entry.getValue());
			}
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
