import java.lang.reflect.Method;

import play.GlobalSettings;
import play.Logger;
import play.api.mvc.Handler;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;


public class Global extends GlobalSettings {

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
	
}
