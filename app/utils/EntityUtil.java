package utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Table;

public class EntityUtil {
	
	public static final Locale DEFAULT_LOCALE = Locale.JAPAN;

	/**
	 * エンティティ entity のテーブル名を取得する
	 * @param entity
	 * @return 取得したテーブル名、失敗時 null
	 */
	public static String getTableName(Class<?> entity) {
		String name = null;
		try {
			name = entity.getAnnotation(Table.class).name();
		} catch (Exception e) { }
		if (name == null) {
			name = classNameToTableName(entity.getSimpleName());
		}
		return name;
	}
	
	
	private static String classNameToTableName(String className) {
		Pattern p = Pattern.compile("([A-Z]+[0-9a-z_]*)");
		Matcher m = p.matcher(className);
		StringBuffer sb = new StringBuffer();
		while(m.find()) {
			String rep = m.group(0).toLowerCase(DEFAULT_LOCALE);
			if (0 < m.start(0)) {
				rep = "_"+rep;
			}
			m.appendReplacement(sb, rep);
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
