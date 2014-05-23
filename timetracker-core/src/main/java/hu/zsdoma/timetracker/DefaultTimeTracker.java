package hu.zsdoma.timetracker;

import hu.zsdoma.timetracker.api.DataSource;
import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
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
        worklogs = new HashMap<>();
    }

    public DefaultTimeTracker(final DataSource dataSource) {
        super();
        this.dataSource = dataSource;
        TimeTrackerEntry database = this.dataSource.load();
        worklogs = database.getWorklogs();
    }

    private void saveCurrentState() {
        if (this.dataSource != null) {
            TimeTrackerEntry timeTrackerEntry = new TimeTrackerEntry(worklogs, this.currentWorklog);
            this.dataSource.save(timeTrackerEntry);
        }
    }

    @Override
    public void addEarlier(final WorklogEntry worklog) {
        check(worklog);
        worklogs.put(worklog.getId(), worklog);
        saveCurrentState();
    }

    private void check(final WorklogEntry worklog) {
        Objects.requireNonNull(worklog, "WorklogEntry is null!");

        long now = new Date().getTime();
        long beginTimestamp = worklog.getBeginTimestamp();
        if (worklog.isProgress()) {
            if (beginTimestamp > now) {
                throw new TimeTrackerException("Invalid begin time. Must be earlier like current date.");
            }
        } else {
            long endTimeStamp = worklog.getEndTimeStamp();
            if ((endTimeStamp > now) || (beginTimestamp >= endTimeStamp)) {
                throw new TimeTrackerException("Invalid begin or end time. Must be earlier like current date.");
            }
            if (overlapCheck(beginTimestamp, endTimeStamp)) {
                throw new TimeTrackerException("Time overlap!");
            }
        }
    }

    @Override
    public WorklogEntry current() {
        return currentWorklog;
    }

    @Override
    public void end(final Date now) {
        Date endDate = DateUtils.normalizeDate(now);
        currentWorklog.setEnd(endDate);
        worklogs.put(currentWorklog.getId(), currentWorklog);
        currentWorklog = null;
        saveCurrentState();
    }

    @Override
    public void end(final Date now, final String message) {
        currentWorklog.setMessage(message);
        end(now);
    }

    @Override
    public WorklogEntry findById(final long id) {
        return worklogs.get(id);
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
    public boolean overlapCheck(final long beginTimestamp, final long endTimeStamp) {
        long normalizedBegin = DateUtils.normalizeTimestamp(beginTimestamp);
        long normalizedEnd = DateUtils.normalizeTimestamp(endTimeStamp);
        for (WorklogEntry worklog : worklogs.values()) {
            long begin = worklog.getBeginTimestamp();
            long end = worklog.getEndTimeStamp();
            if ((begin < normalizedEnd) && (normalizedBegin < end)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeById(final long worklogId) {
        worklogs.remove(worklogId);
        saveCurrentState();
    }

    @Override
    public void start(final String message) {
        startFrom(new Date(), message);
    }

    @Override
    public void startFrom(final Date now, final String message) {
        WorklogEntry worklog = new WorklogEntry(DateUtils.normalizeDate(now), message);
        check(worklog);
        currentWorklog = worklog;
        saveCurrentState();
    }

    @Override
    public void update(final WorklogEntry worklog) {
        worklogs.remove(worklog.getId());
        check(worklog);
        worklogs.put(worklog.getId(), worklog);
        saveCurrentState();
    }

}
