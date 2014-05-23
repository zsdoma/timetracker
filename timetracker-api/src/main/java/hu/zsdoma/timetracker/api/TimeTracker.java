package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.WorklogEntry;

import java.util.Date;
import java.util.List;

/**
 * TimeTracker interface for provide functionality.
 */
public interface TimeTracker {

    /**
     * List all worklogs.
     * 
     * @return List of all stored worklogs.
     */
    List<WorklogEntry> list();

    /**
     * List work logs by given day in timestamp.
     * 
     * @param timestamp
     *            Timestamp // FIXME javadoc
     * @return {@link WorklogEntry} reference list.
     */
    List<WorklogEntry> listByDay(long timestamp);

    WorklogEntry findById(long id);

    void startFrom(Date now, String message);

    void start(String message);

    void end(Date now, String message);

    void end(Date now);

    WorklogEntry current();

    void addEarlier(WorklogEntry worklog);

    /**
     * Remove work log by given worklogId.
     * 
     * @param worklogId
     *            worklogId.
     */
    void removeById(long worklogId);

    /**
     * Update the given worklog.
     * 
     * @param worklog
     *            {@link WorklogEntry} reference.
     */
    void update(WorklogEntry worklog);

    boolean overlapCheck(long beginTimestamp, long endTimeStamp);
}
