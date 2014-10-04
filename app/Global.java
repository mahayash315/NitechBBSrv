import java.lang.reflect.Method;
import java.text.ParseException;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.api.mvc.Handler;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import controllers.task.BBTaskActorBase;


public class Global extends GlobalSettings {
	
	private static ALogger accessLog = Logger.of("accesslog");

	private static final String DEFAULT_ACCESS_LOG_USER_AGENT = "Unknown";
	
	@Override
	public void onStart(Application arg0) {
		super.onStart(arg0);
		
		// BB バッチ処理のスケジュール設定
		try {
			BBTaskActorBase.getInstance().start();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStop(Application arg0) {
		super.onStop(arg0);
		
		// バッチ処理を停止
//		BBAnalyzerTaskActorBase.getInstance().shutdown();
	}

	@Override
	public Action onRequest(Request request, Method actionMethod) {
		
		// アクセスログ出力
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null) {
			userAgent = DEFAULT_ACCESS_LOG_USER_AGENT;
		}
		accessLog.info(request.remoteAddress() + " (" + actionMethod.getName() + ") " + request.method() + " " + request.uri() + " [" + userAgent + "]");

		// デバッグ情報をコンソール表示出力
		try {
			new Debugger().debug(request);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return super.onRequest(request, actionMethod);
	}

	@Override
	public Handler onRouteRequest(RequestHeader arg0) {
		System.out.println("RouteRequest: "+arg0.method()+" "+arg0.path());
		return super.onRouteRequest(arg0);
	}

	@Override
	public Promise<Result> onHandlerNotFound(RequestHeader arg0) {

		// デバッグ情報をコンソール表示出力
		try {
			new Debugger().debug(arg0);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return super.onHandlerNotFound(arg0);
	}
}
