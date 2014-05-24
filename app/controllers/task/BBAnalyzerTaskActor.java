package controllers.task;

import java.util.Set;

import models.entity.User;
import models.service.BBNaiveBayesParam.BBNaiveBayesParamService;
import akka.actor.UntypedActor;

import com.avaje.ebean.Ebean;

public class BBAnalyzerTaskActor extends UntypedActor {
	
	private BBNaiveBayesParamService bbNaiveBayesParamService = new BBNaiveBayesParamService();

	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("Call")) {
			// バッチ処理を行う
			
			calcNaiveBayesParams();
			
		} else {
			unhandled(message);
		}
		
		BBAnalyzerTaskActorBase.getInstance().start();
	}
	
	
	
	private void calcNaiveBayesParams() throws Exception {

		// ユーザ一覧を取得
		Set<User> users = new User().findSet();
		
		// 各ユーザのパラメータを計算
		for(User user : users) {
			Ebean.beginTransaction();
			try {
				// ベイズ推定用パラメータの設定
				bbNaiveBayesParamService.calcParam(user);
				
				Ebean.commitTransaction();
			} catch (Exception e) {
				Ebean.rollbackTransaction();
				throw e;
			} finally {
				Ebean.endTransaction();
			}
		}
	}
}
