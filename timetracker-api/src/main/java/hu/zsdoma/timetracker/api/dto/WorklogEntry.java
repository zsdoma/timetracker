package hu.zsdoma.timetracker.api.dto;

import java.util.Date;

/**
 * DTO class for worklog entry.
 */
public class WorklogEntry implements Comparable<WorklogEntry> {

    /**
     * {@link WorklogEntry} id.
     */
    private long id;

    /**
     * {@link WorklogEntry} begin timestamp.
     */
    private long beginTimestamp = -1;

    /**
     * {@link WorklogEntry} end timestamp.
     */
    private long endTimeStamp = -1;

    /**
     * {@link WorklogEntry} message.
     */
    private String message;

    /**
     * Constructor for set start, end and the message. The id is same as the begin timestamp.
     * 
     * @param start
     *            start of worklog timestamp.
     * @param end
     *            end of worklog timestamp.
     * @param message
     *            Worklog message.
     */
    public WorklogEntry(final Date start, final Date end, final String message) {
        this(start, message);
        this.endTimeStamp = end.getTime();
    }

    /**
     * Constructor for set begint and the message. The id is same as the begin timestamp.
     * 
     * @param start
     *            start of worklog timestamp.
     * @param message
     *            end of worklog timestamp.
     */
    public WorklogEntry(Date start, String message) {
        super();
        this.id = start.getTime();
        this.beginTimestamp = start.getTime();
        this.message = message;
    }

    /**
     * Getter method for worklog begin timestamp.
     * 
     * @return worklog begin timestamp.
     */
    public long getBeginTimestamp() {
        return beginTimestamp;
    }

    /**
     * Getter method for worklog end timestamp.
     * 
     * @return worklog end timestamp.
     */
    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    /**
     * Getter method for worklog start timestamp.
     * 
     * @return worklog begin timestamp.
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for message.
     * 
     * @param message
     *            Message string.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Setter for worklog start date.
     * 
     * @param begin
     *            Worklog start {@link Date}.
     */
    public void setBegin(Date begin) {
        this.beginTimestamp = begin.getTime();
    }

    /**
     * Setter for worklog end date.
     * 
     * @param end
     *            Worklog end {@link Date}.
     */
    public void setEnd(Date end) {
        this.endTimeStamp = end.getTime();
    }

    /**
     * Check whether the worklog is progress.
     * 
     * @return <code>true</code> if progress, <code>false</code> otherwise.
     */
    public boolean isProgress() {
        return endTimeStamp == -1;
    }

    /**
     * Getter method for worklog message.
     * 
     * @return worklog message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * The hashCode method generated with Eclipse.
     * 
     * @return generted hash value.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (beginTimestamp ^ (beginTimestamp >>> 32));
        result = prime * result + (int) (endTimeStamp ^ (endTimeStamp >>> 32));
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }

    /**
     * The equals method generated with Eclipse.
     * 
     * @param obj
     *            The anouther object.
     * @return <code>true</code> if this object equals the another object.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorklogEntry other = (WorklogEntry) obj;
        if (beginTimestamp != other.beginTimestamp)
            return false;
        if (endTimeStamp != other.endTimeStamp)
            return false;
        if (id != other.id)
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "WorklogEntry [id=" + id + ", beginTimestamp=" + beginTimestamp + ", endTimeStamp=" + endTimeStamp
                + ", message=" + message + "]";
    }

    @Override
    public int compareTo(WorklogEntry o) {
        if (this.id < o.getId()) {
            return -1;
        } else if (this.id > o.getId()) {
            return 1;
        }
        return 0;
    }
}
