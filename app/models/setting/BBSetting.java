package models.setting;

public class BBSetting {

	// クラスタの層数。 0 ならユーザごとのみ, 1 以上でクラスタ分類する
	public static final int CLUSTER_DEPTH = 2;	
	// 各層(1,2,...,CLUSTER_DEPTH)のクラスタ数
	public static final int CLUSTER_SIZES[] = new int[]{10, 1};
}
