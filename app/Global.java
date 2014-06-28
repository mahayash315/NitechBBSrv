import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.List;

import models.entity.BBItem;
import models.service.bbitemwordcount.BBItemWordCountService;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.api.mvc.Handler;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import controllers.task.BBAnalyzerTaskActorBase;


public class Global extends GlobalSettings {
	
	private static ALogger accessLog = Logger.of("accesslog");

	private static final String DEFAULT_ACCESS_LOG_USER_AGENT = "Unknown";
	
	@Override
	public void onStart(Application arg0) {
		super.onStart(arg0);
		
		// バッチ処理のスケジュール設定
		try {
			BBAnalyzerTaskActorBase.getInstance().start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// initialize for debugging
		initForDebug();
	}

	@Override
	public Action onRequest(Request request, Method actionMethod) {
		
		// アクセスログ出力
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null) {
			userAgent = DEFAULT_ACCESS_LOG_USER_AGENT;
		}
		accessLog.info(request.remoteAddress() + " (" + actionMethod.getName() + ") " + request.method() + " " + request.uri() + " [" + userAgent + "]");

		// デバッグログ出力
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
	
	
	
	
	
	
	
	private void initForDebug() {
		// BBAnalyzer
		BBItemWordCountService bbItemWordCountService = new BBItemWordCountService();
		List<BBItem> items = new BBItem().find.all();
		for(BBItem item : items) {
			bbItemWordCountService.updateBBItemWordCount(item);
		}
	}
}
