package hu.zsdoma.timetracker;

import hu.zsdoma.timetracker.api.DataSource;
import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link TimeTracker} implementation.
 */
public class DefaultTimeTracker implements TimeTracker {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTimeTracker.class);

    /**
     * {@link DataSource} reference for provide persistent database.
     */
    private DataSource dataSource;

    /**
     * Temporary stored worklogs map, indexed by start timestamp.
     */
    private Map<Long, WorklogEntry> worklogs;

    /**
     * Current worklog reference. Started, but not finished worklog entry.
     */
    private WorklogEntry currentWorklog;

    /**
     * Default constructor. Create a empty worklogs map.
     */
    public DefaultTimeTracker() {
        super();
        worklogs = new HashMap<>();
    }

    /**
     * Constructor with {@link DataSource} referencem to load worklog entries from given datasource.
     * 
     * @param dataSource
     *            {@link DataSource} reference.
     */
    public DefaultTimeTracker(final DataSource dataSource) {
        super();
        this.dataSource = dataSource;
        TimeTrackerEntry database = this.dataSource.load();
        currentWorklog = database.getCurrentWorklog();
        worklogs = database.getWorklogs();
    }

    /**
     * Add earlier worklog instance to current {@link TimeTrackerEntry}. If {@link DataSource} reference not null, then
     * call {@link DataSource#save(TimeTrackerEntry)} method after the new entry is added. Must be earlier like the
     * current date and must be set all fields.
     * 
     * @param worklog
     *            {@link WorklogEntry} reference.
     */
    @Override
    public void addEarlier(final WorklogEntry worklog) {
        checkForInsert(worklog);
        worklogs.put(worklog.getId(), worklog);
        LOGGER.info("Add worklog: " + worklog);
        saveCurrentState();
    }

    /**
     * Check database consistency for insert/update given worklog entry.
     * 
     * @param worklog
     *            {@link WorklogEntry} reference.
     */
    private void checkForInsert(final WorklogEntry worklog) {
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

    /**
     * Check database consistency for start worklog. Called before add new start entry with {@link #start(String)} or
     * {@link #startFrom(Date, String)} methods.
     * 
     * @param worklog
     *            {@link WorklogEntry} reference.
     */
    private void checkForStart(final WorklogEntry worklog) {
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

    /**
     * Return the current {@link WorklogEntry} reference, if exists.
     * 
     * @return The Current {@link WorklogEntry} reference or null.
     */
    @Override
    public WorklogEntry current() {
        return currentWorklog;
    }

    @Override
    public void end() {
        end(null, null);
    }

    @Override
    public void end(final Date now) {
        end(now, null);
    }

    @Override
    public void end(final Date now, final String message) {
        if (message != null) {
            currentWorklog.setMessage(message);
        }
        Date endDate;
        if (now == null) {
            endDate = DateUtils.normalizeDate(new Date());
        } else {
            endDate = DateUtils.normalizeDate(now);
        }
        currentWorklog.setEnd(endDate);
        worklogs.put(currentWorklog.getId(), currentWorklog);
        LOGGER.info(MessageFormat.format("Current worklog finished. [{0}]", currentWorklog));
        currentWorklog = null;
        saveCurrentState();
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
        long startTimeStamp = today.getTimeInMillis();
        today.set(Calendar.HOUR, 23);
        today.set(Calendar.MINUTE, 59);
        long endTimestamp = today.getTimeInMillis();

        List<WorklogEntry> todayWorklogs = new ArrayList<WorklogEntry>();

        List<WorklogEntry> allWorklogs = listAll();
        for (WorklogEntry worklogEntry : allWorklogs) {
            if ((worklogEntry.getBeginTimestamp() >= startTimeStamp)
                    && (worklogEntry.getEndTimeStamp() <= endTimestamp)) {
                todayWorklogs.add(worklogEntry);
            }
        }

        LOGGER.info(MessageFormat.format("Fond {0} worklog(s) by today.", allWorklogs.size()));
        return todayWorklogs;
    }

    // TODO empty list or null?
    @Override
    public List<WorklogEntry> listAll() {
        List<WorklogEntry> worklogs = new ArrayList<WorklogEntry>();
        for (WorklogEntry worklog : this.worklogs.values()) {
            worklogs.add(worklog);
        }
        Collections.sort(worklogs);
        LOGGER.info(MessageFormat.format("Fond {0} worklog(s) of all time.", worklogs.size()));
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
        WorklogEntry removedWorklog = worklogs.remove(worklogId);
        if (removedWorklog == null) {
            LOGGER.warn(MessageFormat.format("Worklog not found by id.", removedWorklog));
        } else {
            LOGGER.info(MessageFormat.format("Worklog removed [{0}]", removedWorklog));
        }
        saveCurrentState();
    }

    /**
     * If the {@link DataSource} is not null, call {@link DataSource#save(TimeTrackerEntry)} method with given
     * timetracker state (with current and all worklogs).
     */
    private void saveCurrentState() {
        if (dataSource != null) {
            LOGGER.info("Datasource fond, persist database.");
            TimeTrackerEntry timeTrackerEntry = new TimeTrackerEntry(worklogs, currentWorklog);
            dataSource.save(timeTrackerEntry);
            LOGGER.info("Database persisted.");
        }
    }

    @Override
    public void start(final String message) {
        startFrom(new Date(), message);
    }

    @Override
    public void startFrom(final Date now, final String message) {
        WorklogEntry worklog = new WorklogEntry(DateUtils.normalizeDate(now), message);
        checkForStart(worklog);
        currentWorklog = worklog;
        LOGGER.info(MessageFormat.format("Start worklog: {0}.", currentWorklog));
        saveCurrentState();
    }

    @Override
    public void update(final WorklogEntry worklog) {
        WorklogEntry oldWorklog = worklogs.remove(worklog.getId());
        checkForInsert(worklog);
        worklogs.put(worklog.getId(), worklog);
        LOGGER.info(MessageFormat.format("Worklog {0} updated with {1}.", oldWorklog, worklog));
        saveCurrentState();
    }

}
