package models.setting;

public class BBSetting {

	/** 各層(1,2,...層)のクラスタ数 */
	public static final int CLUSTER_SIZES[] = new int[]{5, 1};
	
	/** 学習データ作成の際に、「興味あり」と判定する掲示閲覧回数のしきい値 (標準化されたとき) */
	public static final double TRAIN_DATA_CLASSIFICATION_THRESHOLD = 1.29;

	/** ユーザクラスタの初期化が必要かどうか */
	public static final String CONFIGURATION_KEY_REQUIRE_USER_CLUSTER_INITIALIZATION = "bb_require_user_cluster_initialization";
	
	/** 単語リストの最終更新日時 */
	public static final String CONFIGURATION_KEY_WORD_LIST_LAST_MODIFIED = "bb_word_last_modified";
}
