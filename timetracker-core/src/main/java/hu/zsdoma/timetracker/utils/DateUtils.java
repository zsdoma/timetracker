package hu.zsdoma.timetracker.utils;

import java.text.SimpleDateFormat;

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

}
