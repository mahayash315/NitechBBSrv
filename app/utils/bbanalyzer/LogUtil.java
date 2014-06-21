package utils.bbanalyzer;

import play.Logger;
import play.Logger.ALogger;

public class LogUtil {
	private static ALogger logger = Logger.of("bbanalyzer");
	
	public static void info(String message) {
		logger.info(message);
	}

	public static void error(String message) {
		logger.error(message);
	}
	
	public static void error(String message, Throwable error) {
		logger.error(message, error);
	}
}
