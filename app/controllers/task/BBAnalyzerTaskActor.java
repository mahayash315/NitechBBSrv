package controllers.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.service.bbanalyzer.BBAnalyzerService;
import utils.bbanalyzer.LogUtil;
import akka.actor.UntypedActor;

public class BBAnalyzerTaskActor extends UntypedActor {
	
	private BBAnalyzerService bbAnalyzerService = new BBAnalyzerService();;
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("Call")) {
			// バッチ処理を行う
			
//			Map<User, Exception> errors = calcNaiveBayesParams();
//			if (0 < errors.size()) {
//				LogUtil.error("BBAnalyzerTaskActor#onReceive(): errors occurred while executing the task");
//				for(User user : errors.keySet()) {
//					LogUtil.error("user = "+user.toString()+", exeption = "+errors.get(user).getLocalizedMessage());
//				}
//			}
			
			List<Exception> errors = new ArrayList<Exception>();
			
			// classifier
			try {
				classifyUsers();
				trainAllItemClassifiers();
			} catch (Exception e) {
				errors.add(e);
			}
			
			// 処理1
			
			// 処理2
			
			// ...
			
			
			
			// エラー後処理
			if (0 < errors.size()) {
				for(Exception e : errors) {
					LogUtil.error("BBAnalyzerTaskActor#onReceive()", e);
				}
				
				throw new Exception("More than one error occurred while processing tasks.");
			}
			
		} else {
			unhandled(message);
		}
		
		BBAnalyzerTaskActorBase.getInstance().start();
	}
	
	
	private void classifyUsers() throws SQLException {
		bbAnalyzerService.classifyUsers();
	}
	
	private void trainAllItemClassifiers() throws SQLException {
		bbAnalyzerService.trainAllItemClassifiers();
	}
	
//	private Map<User, Exception> calcNaiveBayesParams() throws Exception {
//
//		// ユーザ一覧を取得
//		Set<User> users = new User().findSet();
//		
//		Map<User, Exception> errors = new HashMap<User, Exception>();
//		
//		// 各ユーザのパラメータを計算
//		for(User user : users) {
//			Ebean.beginTransaction();
//			try {
//				// ベイズ推定用パラメータの設定
//				bbAnalyzerService.train(user);
//				
//				Ebean.commitTransaction();
//			} catch (Exception e) {
//				Ebean.rollbackTransaction();
//				errors.put(user, e);
//			} finally {
//				Ebean.endTransaction();
//			}
//		}
//		
//		return errors;
//	}
}
