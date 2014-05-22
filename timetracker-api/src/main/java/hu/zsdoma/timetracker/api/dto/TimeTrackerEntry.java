package hu.zsdoma.timetracker.api.dto;

import java.util.HashMap;
import java.util.Map;

public class TimeTrackerEntry {

    private Map<Long, WorklogEntry> worklogs;

    public TimeTrackerEntry() {
        super();
        this.worklogs = new HashMap<>();
    }

    public TimeTrackerEntry(Map<Long, WorklogEntry> worklogs) {
        super();
        this.worklogs = worklogs;
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
        if (worklogs == null) {
            if (other.worklogs != null)
                return false;
        } else if (!worklogs.equals(other.worklogs))
            return false;
        return true;
    }

}
