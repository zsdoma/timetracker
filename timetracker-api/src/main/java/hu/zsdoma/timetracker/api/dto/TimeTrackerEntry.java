package hu.zsdoma.timetracker.api.dto;

import java.util.HashMap;
import java.util.Map;

public class TimeTrackerEntry {

    private Map<Long, WorklogEntry> worklogs;
    private WorklogEntry currentWorklog;

    public TimeTrackerEntry() {
        super();
        this.worklogs = new HashMap<>();
    }

    public TimeTrackerEntry(Map<Long, WorklogEntry> worklogs, WorklogEntry currentWorklog) {
        super();
        this.worklogs = worklogs;
        this.currentWorklog = currentWorklog;
    }

    public Map<Long, WorklogEntry> getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(Map<Long, WorklogEntry> worklogs) {
        this.worklogs = worklogs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currentWorklog == null) ? 0 : currentWorklog.hashCode());
        result = prime * result + ((worklogs == null) ? 0 : worklogs.hashCode());
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

    public WorklogEntry getCurrentWorklog() {
        return currentWorklog;
    }

    public void setCurrentWorklog(WorklogEntry currentWorklog) {
        this.currentWorklog = currentWorklog;
    }

    @Override
    public String toString() {
        return "TimeTrackerEntry [worklogs=" + worklogs + ", currentWorklog=" + currentWorklog + "]";
    }

}
