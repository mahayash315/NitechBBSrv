package models.service.api.bb;

import java.util.List;

import models.entity.NitechUser;
import models.entity.bb.Possession;
import models.entity.bb.Post;
import models.entity.bb.Word;
import models.request.api.bb.AddPossessionsRequest;
import models.response.api.bb.AddPossessionResponse;
import models.response.api.bb.WordListResponse;
import models.setting.bb.api.BBStatusSetting;

public class BBService {
	// TODO API から利用するサービスを記述
	
	public static BBService use() {
		return new BBService();
	}
	
	/**
	 * 単語リストを返す
	 * @return
	 */
	public WordListResponse procWordList() {
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
	public AddPossessionResponse procAddPossessions(AddPossessionsRequest request) {
		AddPossessionResponse response = null;
		
		if (request.hashedNitechId == null) {
			response = new AddPossessionResponse(BBStatusSetting.BadRequest);
			response.setMessage("null nitech id");
		} else {
			NitechUser nitechUser = new NitechUser(request.hashedNitechId).unique();
			if (nitechUser == null) {
				response = new AddPossessionResponse(BBStatusSetting.BadRequest);
				response.setMessage("invalid nitech user id");
			} else {
				response = new AddPossessionResponse(BBStatusSetting.OK);
				for (AddPossessionsRequest.Entry e : request.possessions) {
					Post post = new Post(e.idDate, e.idIndex).unique();
					if (post != null) {
						Possession possession = new Possession(nitechUser, post);
						if (possession.unique() == null) {
							possession.store();
						}
					}
				}
			}
		}
		
		return response;
	}
}
