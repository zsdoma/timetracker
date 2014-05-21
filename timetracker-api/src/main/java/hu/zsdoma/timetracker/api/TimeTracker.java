package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.WorklogEntry;

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
     * List worklogs by given day in timestamp.
     * 
     * @param timestamp
     *            Timestamp // FIXME javadoc
     * @return {@link WorklogEntry} reference list.
     */
    List<WorklogEntry> listByDay(long timestamp);

    WorklogEntry findById(long id);

    void start(WorklogEntry worklogDTO);

    void end(WorklogEntry worklogDTO);

    void addEarlier(WorklogEntry worklogDTO);

    /**
     * Remove worklog by given worklogId.
     * 
     * @param worklogId
     *            worklogId.
     */
    void removeById(long worklogId);

    /**
     * Update the given worklog.
     * 
     * @param worklogDTO
     *            {@link WorklogEntry} reference.
     */
    void update(WorklogEntry worklogDTO);

    boolean overlapCheck(long beginTimestamp, long endTimeStamp);
}
