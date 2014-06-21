package utils.api.bbanalyzer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.StringTokenizer;

public class DataConverter {
	
	private static final String ENCRYPTION_SALT = "652df351-38e6-dda2-40b4-1609710fb6a4";

	
	public static String getSHA512(String target) throws RuntimeException {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
		} catch (NoSuchAlgorithmException na) {
			throw new RuntimeException("対応しないAlgorithmが設定されました。");
		}

		// MessageDigest初期化
		messageDigest.reset();

		// SALTの追加
		messageDigest.update(ENCRYPTION_SALT.getBytes());

		// さらに、MessageDigestに、今回暗号化対象を追加し、暗号化
		byte[] hash = messageDigest.digest(target.getBytes());

		// 16進数表記の文字列に変換
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			String str = String.format("%02x", b); // 16進数文字列へ変換
			sb.append(str);
		}
		return sb.toString();
	}
	
	
	/**
	 * 指定された日付・時刻文字列を、可能であれば 
	 * Calendarクラスに変換します。
	 * 以下の形式の日付文字列を変換できます。
	 * 
	 * ●変換可能な形式は以下となります。
	 *  yyyy/MM/dd
	 *  yy/MM/dd
	 *  yyyy-MM-dd
	 *  yy-MM-dd
	 *  yyyyMMdd
	 * 
	 * 上記に以下の時間フィールドが組み合わされた状態
	 * でも有効です。
	 *  HH:mm 
	 *  HH:mm:ss 
	 *  HH:mm:ss.SSS
	 *  
	 * @param strDate 日付・時刻文字列。
	 * @return 変換後のCalendarクラス。
	 * @throws IllegalArgumentException 
	 *         日付文字列が変換不可能な場合
	 *         または、矛盾している場合（例：2000/99/99）。
	 */
	public static Calendar toCalendar(String strDate){
	    strDate = format(strDate);
	    Calendar cal = Calendar.getInstance();
	    cal.setLenient(false);

	    int yyyy = Integer.parseInt(strDate.substring(0,4));
	    int MM = Integer.parseInt(strDate.substring(5,7));
	    int dd = Integer.parseInt(strDate.substring(8,10));
	    int HH = cal.get(Calendar.HOUR_OF_DAY);
	    int mm = cal.get(Calendar.MINUTE);
	    int ss = cal.get(Calendar.SECOND);
	    int SSS = cal.get(Calendar.MILLISECOND);
	    cal.clear();
	    cal.set(yyyy,MM-1,dd);
	    int len = strDate.length();
	    switch (len) {
	        case 10:
	            break;
	        case 16: // yyyy/MM/dd HH:mm
	            HH = Integer.parseInt(strDate.substring(11,13));
	            mm = Integer.parseInt(strDate.substring(14,16));
	            cal.set(Calendar.HOUR_OF_DAY,HH);
	            cal.set(Calendar.MINUTE,mm);
	            break;
	        case 19: //yyyy/MM/dd HH:mm:ss
	            HH = Integer.parseInt(strDate.substring(11,13));
	            mm = Integer.parseInt(strDate.substring(14,16));
	            ss = Integer.parseInt(strDate.substring(17,19));
	            cal.set(Calendar.HOUR_OF_DAY,HH);
	            cal.set(Calendar.MINUTE,mm);
	            cal.set(Calendar.SECOND,ss);
	            break;
	        case 23: //yyyy/MM/dd HH:mm:ss.SSS
	            HH = Integer.parseInt(strDate.substring(11,13));
	            mm = Integer.parseInt(strDate.substring(14,16));
	            ss = Integer.parseInt(strDate.substring(17,19));
	            SSS = Integer.parseInt(strDate.substring(20,23));
	            cal.set(Calendar.HOUR_OF_DAY,HH);
	            cal.set(Calendar.MINUTE,mm);
	            cal.set(Calendar.SECOND,ss);
	            cal.set(Calendar.MILLISECOND,SSS);
	            break;
	        default :
	            throw new IllegalArgumentException(
	                    "引数の文字列["+ strDate +
	                    "]は日付文字列に変換できません");
	    }
	    return cal;
	}

	/**
	 * 様々な日付、時刻文字列をデフォルトの日付・時刻フォーマット
	 * へ変換します。
	 * 
	 * ●デフォルトの日付フォーマットは以下になります。
	 *     日付だけの場合：yyyy/MM/dd
	 *     日付+時刻の場合：yyyy/MM/dd HH:mm:ss.SSS
	 * 
	 * @param str 変換対象の文字列
	 * @return デフォルトの日付・時刻フォーマット
	 * @throws IllegalArgumentException 
	 *     日付文字列が変換不可能な場合 
	 */
	private static String format(String str){
	    if (str == null || str.trim().length() < 8) {
	        throw new IllegalArgumentException(
	                "引数の文字列["+ str +
	                "]は日付文字列に変換できません");
	    }
	    str = str.trim();
	    String yyyy = null; String MM = null; String dd = null; 
	    String HH = null; String mm = null; 
	    String ss = null;String SSS = null;  
	    // "-" or "/" が無い場合
	    if (str.indexOf("/")==-1 && str.indexOf("-")==-1) {
	        if (str.length() == 8) {
	            yyyy = str.substring(0,4);
	            MM = str.substring(4,6);
	            dd = str.substring(6,8);
	            return yyyy+"/"+MM+"/"+dd;
	        }
	        yyyy = str.substring(0,4);
	        MM = str.substring(4,6);
	        dd = str.substring(6,8);
	        HH = str.substring(9,11);
	        mm = str.substring(12,14);
	        ss = str.substring(15,17);
	        return yyyy+"/"+MM+"/"+dd+" "+HH+":"+mm+":"+ss;
	    }
	    StringTokenizer token = new StringTokenizer(str,"_/-:. ");
	    StringBuffer result = new StringBuffer();
	    for(int i = 0; token.hasMoreTokens(); i++) {
	        String temp = token.nextToken();
	        switch(i){
	            case 0:// 年の部分
	                yyyy = fillString(str, temp, "L", "20", 4);
	                result.append(yyyy);  
	                break;
	            case 1:// 月の部分
	                MM = fillString(str, temp, "L", "0", 2); 
	                result.append("/"+MM);  
	                break;
	            case 2:// 日の部分
	                dd = fillString(str, temp, "L", "0", 2); 
	                result.append("/"+dd);  
	                break;
	            case 3:// 時間の部分
	                HH = fillString(str, temp, "L", "0", 2);
	                result.append(" "+HH);  
	                break;
	            case 4:// 分の部分
	                mm = fillString(str, temp, "L", "0", 2); 
	                result.append(":"+mm);  
	                break;
	            case 5:// 秒の部分
	                ss = fillString(str, temp, "L", "0", 2); 
	                result.append(":"+ss);  
	                break;
	            case 6:// ミリ秒の部分
	                SSS = fillString(str, temp, "R", "0", 3); 
	                result.append("."+SSS);  
	                break;
	        }
	    }
	    return result.toString();
	}
	private static String fillString(String strDate, String str, 
	                             String position, String addStr, 
	                             int len){
	    if (str.length() > len) {
	        throw new IllegalArgumentException(
	            "引数の文字列["+ strDate +
	            "]は日付文字列に変換できません");
	    }
	    return fillString(str, position, len,addStr);
	}

	/**
	 * 文字列[str]に対して、補充する文字列[addStr]を 
	 * [position]の位置に[len]に満たすまで挿入します。
	 * 
	 * ※[str]がnullや空リテラルの場合でも[addStr]を
	 * [len]に満たすまで挿入した結果を返します。
	 * @param str 対象文字列
	 * @param position 前に挿入 ⇒ L or l 後に挿入 ⇒ R or r
	 * @param len 補充するまでの桁数
	 * @param addStr 挿入する文字列
	 * @return 変換後の文字列。
	 */
	private static String fillString(String str, String position, 
	        int len,
	        String addStr) {
	    if (addStr == null || addStr.length() == 0) {
	        throw new IllegalArgumentException
	            ("挿入する文字列の値が不正です。addStr="+addStr);
	    }
	    if (str == null) {
	        str = "";
	    }
	    StringBuffer buffer = new StringBuffer(str);
	    while (len > buffer.length()) {
	        if (position.equalsIgnoreCase("l")) {
	            int sum = buffer.length() + addStr.length();
	            if (sum > len) {
	                addStr = addStr.substring
	                    (0,addStr.length() - (sum - len));
	                buffer.insert(0, addStr);
	            }else{
	                buffer.insert(0, addStr);
	            }
	        } else {
	            buffer.append(addStr);
	        }
	    }
	    if (buffer.length() == len) {
	        return buffer.toString();
	    }
	    return buffer.toString().substring(0, len);
	}
}
