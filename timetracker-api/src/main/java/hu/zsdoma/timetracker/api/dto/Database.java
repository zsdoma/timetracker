package hu.zsdoma.timetracker.api.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {

    private Map<Long, Worklog> worklogs;
    private List<Long> timestampIndex;
    private List<Boolean> worklogTypes;

    public Database() {
        super();
        this.worklogs = new HashMap<>();
    }

    public Database(Map<Long, Worklog> worklogs) {
        super();
        this.worklogs = worklogs;
    }

    public Map<Long, Worklog> getWorklogs() {
        return worklogs;
    }

    public void setWorklogs(Map<Long, Worklog> worklogs) {
        this.worklogs = worklogs;
    }

}
