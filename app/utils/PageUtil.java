package utils;

public class PageUtil {

	/**
	 * 取得したいページ番号を Ebean に引数として渡すページ番号に変換する
	 * @param i
	 * @return
	 */
	public static Integer rightPage(Integer i) {
		return (i != null && 0 < i) ? (i-1) : 0;
	}
}
