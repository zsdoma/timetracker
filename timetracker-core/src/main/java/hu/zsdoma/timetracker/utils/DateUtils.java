package hu.zsdoma.timetracker.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    public static final String DATE_FORMAT = "yyyy.MM.dd. HH:mm";

    /**
     * Default utility constructor.
     */
    private DateUtils() {
        super();
    }

    public static final SimpleDateFormat dateFormatInstance() {
        return new SimpleDateFormat(DATE_FORMAT);
    }

    public static long normalizeTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Date normalizeDate(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

}
