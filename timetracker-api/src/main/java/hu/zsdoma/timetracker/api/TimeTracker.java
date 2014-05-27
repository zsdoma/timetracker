package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.WorklogEntry;

import java.util.Date;
import java.util.List;

/**
 * TimeTracker interface for provide functionality.
 */
public interface TimeTracker {

    /**
     * List worklogs by current day.
     * 
     * @return List of {@link WorklogEntry} by current date or empty list.
     */
    List<WorklogEntry> list();

    /**
     * List worklogs by given day.
     * 
     * @param timestamp
     *            The given day in timestamp.
     * @return List of {@link WorklogEntry} by the given date or empty list.
     */
    List<WorklogEntry> listByDay(long timestamp);

    /**
     * List all stored worklogs.
     * 
     * @return List of all {@link WorklogEntry} or empty list.
     */
    List<WorklogEntry> listAll();

    /**
     * Find a worklog by given id.
     * 
     * @param id
     *            Given {@link WorklogEntry} id.
     * @return Found {@link WorklogEntry} or null.
     */
    WorklogEntry findById(long id);

    /**
     * Start worklog with given message from given date.
     * 
     * @param now
     *            The {@link Date} when worklog start.
     * @param message
     *            Worklog message.
     */
    void startFrom(Date now, String message);

    /**
     * Start worklog with given message.
     * 
     * @param message
     *            Worklog message.
     */
    void start(String message);

    /**
     * Finish current worklog with given date and message.
     * 
     * @param now
     *            {@link Date} of worklog end.
     * @param message
     *            Finished worklog message. Update latest worklog message.
     */
    void end(Date now, String message);

    /**
     * Finish current worklog with given date.
     * 
     * @param now
     *            {@link Date} of worklog end.
     */
    void end(Date now);

    /**
     * Finish current worklog with current date.
     */
    void end();

    /**
     * Get current worklog or null if current worklog reference null.
     * 
     * @return Current {@link WorklogEntry} reference.
     */
    WorklogEntry current();

    /**
     * Add a earlier worklog.
     * 
     * @param worklog
     *            Earlier {@link WorklogEntry} reference.
     */
    void addEarlier(WorklogEntry worklog);

    /**
     * Remove work log by given worklogId.
     * 
     * @param worklogId
     *            worklogId.
     */
    void removeById(long worklogId);

    /**
     * Update a stored {@link WorklogEntry} with given worklog by given worklog id.
     * 
     * @param worklog
     *            {@link WorklogEntry} reference.
     */
    void update(WorklogEntry worklog);

    /**
     * Overlap check for the given start-end intervall in timestamp.
     * 
     * @param beginTimestamp
     *            Start timestamp.
     * @param endTimeStamp
     *            End timestamp.
     * @return <code>true</code> if the given interval overlap a existed {@link WorklogEntry}.
     */
    boolean overlapCheck(long beginTimestamp, long endTimeStamp);
}
