package hu.zsdoma.timetracker;

import hu.zsdoma.timetracker.api.DataSource;
import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Default {@link TimeTracker} implementation.
 */
public class DefaultTimeTracker implements TimeTracker {

    private DataSource dataSource;
    private Map<Long, WorklogEntry> worklogs;
    private WorklogEntry currentWorklog;

    public DefaultTimeTracker() {
        super();
        this.worklogs = new HashMap<>();
    }

    public DefaultTimeTracker(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
        TimeTrackerEntry database = this.dataSource.load();
        this.worklogs = database.getWorklogs();
    }

    @Override
    public List<WorklogEntry> list() {
        List<WorklogEntry> worklogs = new ArrayList<WorklogEntry>();
        for (WorklogEntry worklog : this.worklogs.values()) {
            worklogs.add(worklog);
        }
        Collections.sort(worklogs);
        return worklogs;
    }

    @Override
    public List<WorklogEntry> listByDay(final long timestamp) {
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

    @Override
    public void addEarlier(final WorklogEntry worklog) {
        check(worklog);
        this.worklogs.put(worklog.getId(), worklog);
    }

    private void check(WorklogEntry worklog) {
        Objects.requireNonNull(worklog, "WorklogEntry is null!");

        long now = new Date().getTime();
        long beginTimestamp = worklog.getBeginTimestamp();
        long endTimeStamp = worklog.getEndTimeStamp();

        if (endTimeStamp > now || beginTimestamp >= endTimeStamp) {
            throw new TimeTrackerException("Invalid begin or end time. Must be earlier as current date.");
        }

        if (overlapCheck(beginTimestamp, endTimeStamp)) {
            throw new TimeTrackerException("Time overlap!");
        }
    }

    @Override
    public boolean overlapCheck(long beginTimestamp, long endTimeStamp) {
        for (WorklogEntry worklog : this.worklogs.values()) {
            long begin = worklog.getBeginTimestamp();
            long end = worklog.getEndTimeStamp();
            if (begin < endTimeStamp && beginTimestamp < end) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeById(final long worklogId) {
        this.worklogs.remove(worklogId);
    }

    @Override
    public void update(final WorklogEntry worklog) {
        this.worklogs.remove(worklog.getId());
        check(worklog);
        this.worklogs.put(worklog.getId(), worklog);
    }

    @Override
    public void start(WorklogEntry worklog) {
        check(worklog);
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

    @Override
    public void end(WorklogEntry worklog) {
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

    @Override
    public WorklogEntry findById(long id) {
        return this.worklogs.get(id);
    }

}
