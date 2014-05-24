import java.lang.reflect.Method;
import java.text.ParseException;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.api.mvc.Handler;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import controllers.task.BBAnalyzerTaskActorBase;


public class Global extends GlobalSettings {

	@Override
	public void onStart(Application arg0) {
		super.onStart(arg0);
		
		// バッチ処理のスケジュール設定
		try {
			BBAnalyzerTaskActorBase.getInstance().start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Action onRequest(Request request, Method actionMethod) {
		
		Debugger.callOnRequest(request);
		
		return super.onRequest(request, actionMethod);
	}

	@Override
	public Handler onRouteRequest(RequestHeader request) {

		Logger.debug("RouteRequest: "+request.uri());
		
		return super.onRouteRequest(request);
	}

	@Override
	public void onStop(Application arg0) {
		super.onStop(arg0);
		
		// バッチ処理を停止
		BBAnalyzerTaskActorBase.getInstance().shutdown();
	}
	
}
