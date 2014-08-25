package models.service.bb;

import models.setting.BBSetting;
import play.Logger;

import com.avaje.ebean.CallableSql;
import com.avaje.ebean.Ebean;


public class UserClassifier {
	
	public static UserClassifier use() {
		return new UserClassifier();
	}
	

	/**
	 * 第 1 層以上のクラスタを初期化する（作りなおす）
	 */
	public void initClusters() {

		Ebean.beginTransaction();
		try {
			for (int i = 0; i < BBSetting.CLUSTER_SIZES.length; ++i) {
				CallableSql cSql1 = Ebean.createCallableSql("{call InitKMeansFor(?,?)}");
				cSql1.setParameter(1, i+1);
				cSql1.setParameter(2, BBSetting.CLUSTER_SIZES[i]);
				int r1 = Ebean.execute(cSql1);
				
				CallableSql cSql2 = Ebean.createCallableSql("{call ClassifyClustersFor(?)}");
				cSql2.setParameter(1, i+1);
				int r2 = Ebean.execute(cSql2);
			}
            Ebean.execute(Ebean.createCallableSql("commit;"));
			Ebean.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(UserClassifier.class.getName()+"#initClusters()", e);
			Ebean.rollbackTransaction();
		} finally {
			Ebean.endTransaction();
		}
		
	}
}
