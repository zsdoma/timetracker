package hu.zsdoma.timetracker.api.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private Map<Long, WorklogEntry> worklogs;
    private List<Long> timestampIndex;
    private List<Boolean> worklogTypes;

    public Database() {
        super();
        this.worklogs = new HashMap<>();
    }

    public Database(Map<Long, WorklogEntry> worklogs) {
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
