package hu.zsdoma.timetracker.api.exception;

public class TimeTrackerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TimeTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeTrackerException(String message) {
        super(message);
    }

}
