package hu.zsdoma.timetracker.api.dto;

import java.util.Date;

/**
 * DTO class for worklog entry.
 */
public class Worklog implements Comparable<Worklog> {

    /**
     * Worklog ID.
     */
    private long id;

    /**
     * Worklog begin timestamp.
     */
    private long beginTimestamp;

    /**
     * Worklog end timestamp.
     */
    private long endTimeStamp;

    /**
     * Worklog message.
     */
    private String message;

    /**
     * Constructor for setting all member variable.
     * 
     * @param begin
     * @param end
     * @param message
     * @param issueId
     */
    // TODO builder patter
    public Worklog(final Date begin, final Date end, final String message) {
        super();
        this.beginTimestamp = begin.getTime();
        this.endTimeStamp = end.getTime();
        this.id = this.beginTimestamp;
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

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setBegin(Date begin) {
        this.beginTimestamp = begin.getTime();
    }
    
    public void setEnd(Date end) {
        this.endTimeStamp = end.getTime();
    }

    /**
     * Getter method for worklog message.
     * 
     * @return worklog message.
     */
    public String getMessage() {
        return message;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Worklog other = (Worklog) obj;
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
        return "Worklog [id=" + id + ", beginTimestamp=" + beginTimestamp + ", endTimeStamp=" + endTimeStamp
                + ", message=" + message + "]";
    }

    @Override
    public int compareTo(Worklog o) {
        if (this.id < o.getId()) {
            return -1;
        } else if (this.id > o.getId()) {
            return 1;
        }
        return 0;
    }
}
