/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Eko SW
 */
public class DateUtils {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());

    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }

    public static SimpleDateFormat nowAsSimpleDateFormat() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);

        return sdf;

    }
}
