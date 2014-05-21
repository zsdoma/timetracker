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

}
