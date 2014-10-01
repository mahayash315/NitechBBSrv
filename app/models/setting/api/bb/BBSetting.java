package models.setting.api.bb;

public class BBSetting {
	public static double RELEVANT_POST_DISTANCE_THRESHOLD = 0.3;
	public static int RELEVANT_POST_MAX_ROW_NUM = 5;
	public static final Long POPULAR_POSTS_DEFAULT_THRESHOLD = Long.valueOf(10);
	public static final long POPULAR_POSTS_DELTA_TIMESTAMP = 24*60*60*1000;
}
