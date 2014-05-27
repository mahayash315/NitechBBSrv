package controllers.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import models.entity.User;
import models.service.BBAnalyzer.BBAnalyzerService;
import utils.api.bbanalyzer.LogUtil;
import akka.actor.UntypedActor;

import com.avaje.ebean.Ebean;

public class BBAnalyzerTaskActor extends UntypedActor {
	
	private BBAnalyzerService bbAnalyzerService = new BBAnalyzerService();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("Call")) {
			// バッチ処理を行う
			
			Map<User, Exception> errors = calcNaiveBayesParams();
			if (0 < errors.size()) {
				LogUtil.error("BBAnalyzerTaskActor#onReceive(): errors occurred while executing the task");
				for(User user : errors.keySet()) {
					LogUtil.error("user = "+user.toString()+", exeption = "+errors.get(user).getLocalizedMessage());
				}
			}
			
		} else {
			unhandled(message);
		}
		
		BBAnalyzerTaskActorBase.getInstance().start();
	}
	
	
	
	private Map<User, Exception> calcNaiveBayesParams() throws Exception {

		// ユーザ一覧を取得
		Set<User> users = new User().findSet();
		
		Map<User, Exception> errors = new HashMap<User, Exception>();
		
		// 各ユーザのパラメータを計算
		for(User user : users) {
			Ebean.beginTransaction();
			try {
				// ベイズ推定用パラメータの設定
				bbAnalyzerService.train(user);
				
				Ebean.commitTransaction();
			} catch (Exception e) {
				Ebean.rollbackTransaction();
				errors.put(user, e);
			} finally {
				Ebean.endTransaction();
			}
		}
		
		return errors;
	}
}
