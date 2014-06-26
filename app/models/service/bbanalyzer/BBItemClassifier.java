package models.service.bbanalyzer;

import models.entity.BBItem;

public class BBItemClassifier {
	
	// この識別器の取り付け先クラスタ
	UserCluster userCluster;
	
	private BBItemClassifier() {
	}
	public BBItemClassifier(UserCluster userCluster) {
		this();
		this.userCluster = userCluster;
	}
	
	/**
	 * クラスタ内の全ユーザの掲示閲覧履歴を学習データとして、好みの掲示を学習する
	 */
	public void train() {
		// TODO implement here
	}
	
	/**
	 * 掲示 item をクラス分類する
	 * @param item
	 */
	public void classify(BBItem item) {
		// TODO implement here
	}
	
}
