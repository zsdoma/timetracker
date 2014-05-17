package hu.zsdoma.timetracker;

import java.util.List;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogDTO;

/**
 * Default {@link TimeTracker} implementation.
 */
public class DefaultTimeTracker implements TimeTracker {

	@Override
	public void newWorklog(WorklogDTO worklogDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateWorklog(WorklogDTO worklogDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeWorklog(long worklogId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<WorklogDTO> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorklogDTO> listByDay(long timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WorklogDTO> listByIssue(long issueId) {
		// TODO Auto-generated method stub
		return null;
	}

}
