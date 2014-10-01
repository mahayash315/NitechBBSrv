package models.actor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controllers.task.BBAnalyzerTaskActorBase;
import models.service.bbanalyzer.BBAnalyzerService;
import utils.bbanalyzer.LogUtil;
import akka.actor.UntypedActor;

public class BBAnalyzerTaskActor extends UntypedActor {
	
	private BBAnalyzerService bbAnalyzerService = new BBAnalyzerService();;
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message.equals("Call")) {
			// バッチ処理を行う
			
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
		bbAnalyzerService.classifyAllUsers();
	}
	
	private void trainAllItemClassifiers() throws SQLException {
		bbAnalyzerService.trainAllItemClassifiers();
	}
}
