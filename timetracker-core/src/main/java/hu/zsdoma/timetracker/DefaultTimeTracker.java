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
        currentWorklog = database.getCurrentWorklog();
        worklogs = database.getWorklogs();
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
        long endTimeStamp = worklog.getEndTimeStamp();
        if ((endTimeStamp > now) || (beginTimestamp >= endTimeStamp)) {
            throw new TimeTrackerException("Invalid begin or end time. Must be earlier like current date.");
        }
        if (overlapCheck(beginTimestamp, endTimeStamp)) {
            throw new TimeTrackerException("Time overlap!");
        }
    }

    private void checkStart(final WorklogEntry worklog) {
        Objects.requireNonNull(worklog, "WorklogEntry is null!");

        long now = new Date().getTime();
        long beginTimestamp = worklog.getBeginTimestamp();

        if (this.currentWorklog != null) {
            throw new TimeTrackerException("A worklog already started.");
        }

        if (!worklog.isProgress()) {
            throw new TimeTrackerException("Worklog has endTime!");
        }
        if (beginTimestamp > now) {
            throw new TimeTrackerException("Invalid begin time. Must be earlier like current date.");
        }
    }

    @Override
    public WorklogEntry current() {
        return currentWorklog;
    }

    @Override
    public void end() {
        throw new UnsupportedOperationException("Not implemented, yet.");
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
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.HOUR, 23);
        today.set(Calendar.MINUTE, 59);
        long startTimeStamp = today.getTimeInMillis();
        long endTimestamp = today.getTimeInMillis();

        List<WorklogEntry> todayWorklogs = new ArrayList<WorklogEntry>();

        List<WorklogEntry> allWorklogs = listAll();
        for (WorklogEntry worklogEntry : allWorklogs) {
            if ((worklogEntry.getBeginTimestamp() >= startTimeStamp)
                    && (worklogEntry.getEndTimeStamp() <= endTimestamp)) {
                todayWorklogs.add(worklogEntry);
            }
        }

        return todayWorklogs;
    }

    @Override
    public List<WorklogEntry> listAll() {
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

    private void saveCurrentState() {
        if (dataSource != null) {
            TimeTrackerEntry timeTrackerEntry = new TimeTrackerEntry(worklogs, currentWorklog);
            dataSource.save(timeTrackerEntry);
        }
    }

    @Override
    public void start(final String message) {
        startFrom(new Date(), message);
    }

    @Override
    public void startFrom(final Date now, final String message) {
        WorklogEntry worklog = new WorklogEntry(DateUtils.normalizeDate(now), message);
        checkStart(worklog);
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
