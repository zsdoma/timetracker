package hu.zsdoma.timetracker.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility methods for date manage.
 */
public final class DateUtils {

    /**
     * Date format pattern.
     */
    public static final String DATE_FORMAT = "yyyy.MM.dd. HH:mm";

    /**
     * Default utility constructor.
     */
    private DateUtils() {
        super();
    }

    /**
     * Create a {@link SimpleDateFormat} instance with {@link #DATE_FORMAT} pattern.
     * 
     * @return {@link SimpleDateFormat} reference.
     */
    public static final SimpleDateFormat dateFormatInstance() {
        return new SimpleDateFormat(DATE_FORMAT);
    }

    /**
     * Normalize the timestamp minute precision. (set second to zero).
     * 
     * @param timestamp
     *            timestamp for normalize.
     * 
     * @return Minute precision timestamp
     */
    public static long normalizeTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * Normalize the Date minute precision. (set second to zero).
     * 
     * @param date
     *            Date instance for normalize.
     * @return Minute precision timestamp.
     */
    public static Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

}
