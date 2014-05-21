package hu.zsdoma.timetracker;

import hu.zsdoma.timetracker.api.DataSource;
import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.Database;
import hu.zsdoma.timetracker.api.dto.Worklog;
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
    private Map<Long, Worklog> worklogs;
    private Worklog currentWorklog;

    public DefaultTimeTracker() {
        super();
        this.worklogs = new HashMap<>();
    }

    public DefaultTimeTracker(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
        Database database = this.dataSource.load();
        this.worklogs = database.getWorklogs();
    }

    @Override
    public List<Worklog> list() {
        List<Worklog> worklogs = new ArrayList<Worklog>();
        for (Worklog worklog : this.worklogs.values()) {
            worklogs.add(worklog);
        }
        Collections.sort(worklogs);
        return worklogs;
    }

    @Override
    public List<Worklog> listByDay(final long timestamp) {
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

    @Override
    public void addEarlier(final Worklog worklog) {
        check(worklog);
        this.worklogs.put(worklog.getId(), worklog);
    }

    private void check(Worklog worklog) {
        Objects.requireNonNull(worklog, "Worklog is null!");

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
        for (Worklog worklog : this.worklogs.values()) {
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
    public void update(final Worklog worklog) {
        this.worklogs.remove(worklog.getId());
        check(worklog);
        this.worklogs.put(worklog.getId(), worklog);
    }

    @Override
    public void start(Worklog worklog) {
        check(worklog);
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

    @Override
    public void end(Worklog worklog) {
        throw new UnsupportedOperationException("Not implemented, yet.");
    }

    @Override
    public Worklog findById(long id) {
        return this.worklogs.get(id);
    }

}
