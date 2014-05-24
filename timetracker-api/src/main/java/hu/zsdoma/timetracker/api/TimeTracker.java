package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.WorklogEntry;

import java.util.Date;
import java.util.List;

/**
 * TimeTracker interface for provide functionality.
 */
public interface TimeTracker {

    List<WorklogEntry> list();

    List<WorklogEntry> listByDay(long timestamp);

    List<WorklogEntry> listAll();

    WorklogEntry findById(long id);

    void startFrom(Date now, String message);

    void start(String message);

    void end(Date now, String message);

    void end(Date now);

    void end();

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
