package models.view.mockbb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	public static String formatDefaultDate(Date date) {
		if (date != null) {
			return new SimpleDateFormat("yyyy/MM/dd").format(date);
		}
		return null;
	}
}
