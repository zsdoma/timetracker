package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.Worklog;

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
    List<Worklog> list();

    /**
     * List worklogs by given day in timestamp.
     * 
     * @param timestamp
     *            Timestamp // FIXME javadoc
     * @return {@link Worklog} reference list.
     */
    List<Worklog> listByDay(long timestamp);

    Worklog findById(long id);

    void start(Worklog worklogDTO);

    void end(Worklog worklogDTO);

    void addEarlier(Worklog worklogDTO);

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
     *            {@link Worklog} reference.
     */
    void update(Worklog worklogDTO);

    boolean overlapCheck(long beginTimestamp, long endTimeStamp);
}
