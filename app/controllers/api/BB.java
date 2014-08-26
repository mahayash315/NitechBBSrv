package controllers.api;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class BB extends Controller {

	/**
	 * 単語リストを返すアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result wordList() {
		return TODO;
	}
	
	/**
	 * 新規掲示を追加するアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result addPossessions() {
		return TODO;
	}
	
	/**
	 * 掲示を消すアクション (invalidate)
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result deletePossessions() {
		return TODO;
	}
	
	/**
	 * 掲示閲覧履歴を追加するアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result storeHistories() {
		return TODO;
	}
	
	/**
	 * お勧め掲示を返すアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result suggestions() {
		return TODO;
	}
	
	/**
	 * 関連掲示を返すアクション
	 * @return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result relevants() {
		return TODO;
	}
}
