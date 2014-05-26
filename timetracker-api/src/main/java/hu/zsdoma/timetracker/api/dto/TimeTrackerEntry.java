package hu.zsdoma.timetracker.api.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple time tracker database representation.
 */
public class TimeTrackerEntry {

    /**
     * Map of {@link WorklogEntry}s.
     */
    private Map<Long, WorklogEntry> worklogs;

    /**
     * Current {@link WorklogEntry}.
     */
    private WorklogEntry currentWorklog;

    /**
     * Default constuctor. Initialize a empty worklog map.
     */
    public TimeTrackerEntry() {
        super();
        this.worklogs = new HashMap<>();
    }

    /**
     * Constructor for set worklogs and current worklog.
     * 
     * @param worklogs
     *            Map of {@link WorklogEntry}s.
     * @param currentWorklog
     *            the current {@link WorklogEntry}.
     */
    public TimeTrackerEntry(Map<Long, WorklogEntry> worklogs, WorklogEntry currentWorklog) {
        super();
        this.worklogs = worklogs;
        this.currentWorklog = currentWorklog;
    }

    /**
     * Getter worklog map.
     * 
     * @return Map of {@link WorklogEntry}s.
     */
    public Map<Long, WorklogEntry> getWorklogs() {
        return worklogs;
    }

    /**
     * Setter for worklogs.
     * 
     * @param worklogs
     *            Map of {@link WorklogEntry}s.
     */
    public void setWorklogs(Map<Long, WorklogEntry> worklogs) {
        this.worklogs = worklogs;
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
        result = prime * result + ((currentWorklog == null) ? 0 : currentWorklog.hashCode());
        result = prime * result + ((worklogs == null) ? 0 : worklogs.hashCode());
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
        TimeTrackerEntry other = (TimeTrackerEntry) obj;
        if (currentWorklog == null) {
            if (other.currentWorklog != null)
                return false;
        } else if (!currentWorklog.equals(other.currentWorklog))
            return false;
        if (worklogs == null) {
            if (other.worklogs != null)
                return false;
        } else if (!worklogs.equals(other.worklogs))
            return false;
        return true;
    }

    /**
     * Getter for current worklog.
     * 
     * @return current {@link WorklogEntry}.
     */
    public WorklogEntry getCurrentWorklog() {
        return currentWorklog;
    }

    /**
     * Setter for current worklog.
     * 
     * @param currentWorklog
     *            current {@link WorklogEntry}.
     */
    public void setCurrentWorklog(WorklogEntry currentWorklog) {
        this.currentWorklog = currentWorklog;
    }

    @Override
    public String toString() {
        return "TimeTrackerEntry [worklogs=" + worklogs + ", currentWorklog=" + currentWorklog + "]";
    }

}
