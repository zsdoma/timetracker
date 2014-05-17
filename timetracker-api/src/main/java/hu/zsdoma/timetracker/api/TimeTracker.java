package hu.zsdoma.timetracker.api;

import java.util.List;

import hu.zsdoma.timetracker.api.dto.WorklogDTO;

/**
 * TimeTracker interface for provide functionality.
 */
public interface TimeTracker {

	/**
	 * Persist the given worklog.
	 * 
	 * @param worklogDTO
	 *            Given {@link WorklogDTO} reference.
	 */
	void newWorklog(WorklogDTO worklogDTO);

	/**
	 * Update the given worklog.
	 * 
	 * @param worklogDTO
	 *            {@link WorklogDTO} reference.
	 */
	void updateWorklog(WorklogDTO worklogDTO);

	/**
	 * Remove worklog by given worklogId.
	 * 
	 * @param worklogId
	 *            worklogId.
	 */
	void removeWorklog(long worklogId);

	/**
	 * List all worklogs.
	 * 
	 * @return List of all stored worklogs.
	 */
	List<WorklogDTO> list();

	/**
	 * List worklogs by given day in timestamp.
	 * 
	 * @param timestamp Timestamp // FIXME javadoc
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
}
