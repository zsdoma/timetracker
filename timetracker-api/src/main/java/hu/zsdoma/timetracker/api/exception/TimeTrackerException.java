package hu.zsdoma.timetracker.api.exception;

/**
 * RuntimeException for time tracker errors.
 */
public class TimeTrackerException extends RuntimeException {
    /**
     * Default serial version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor with message and cause exception class.
     * 
     * @param message
     *            Exception message.
     * @param cause
     *            {@link Throwable} instance.
     */
    public TimeTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message.
     * 
     * @param message
     *            Exception message.
     */
    public TimeTrackerException(String message) {
        super(message);
    }

}
