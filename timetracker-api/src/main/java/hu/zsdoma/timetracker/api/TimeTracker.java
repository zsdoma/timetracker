package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.WorklogDTO;

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
    List<WorklogDTO> list();

    /**
     * List worklogs by given day in timestamp.
     * 
     * @param timestamp
     *            Timestamp // FIXME javadoc
     * @return {@link WorklogDTO} reference list.
     */
    List<WorklogDTO> listByDay(long timestamp);

    /**
     * List worklogs by given issue id.
     * 
     * @param issueId
     *            Issue id.
     * @return {@link WorklogDTO} reference list.
     */
    List<WorklogDTO> listByIssue(long issueId);

    /**
     * Persist the given worklog.
     * 
     * @param worklogDTO
     *            Given {@link WorklogDTO} reference.
     */
    void newWorklog(WorklogDTO worklogDTO);

    /**
     * Remove worklog by given worklogId.
     * 
     * @param worklogId
     *            worklogId.
     */
    void removeWorklog(long worklogId);

    /**
     * Update the given worklog.
     * 
     * @param worklogDTO
     *            {@link WorklogDTO} reference.
     */
    void updateWorklog(WorklogDTO worklogDTO);
}
