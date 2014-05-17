package hu.zsdoma.timetracker;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogDTO;

import java.util.List;

/**
 * Default {@link TimeTracker} implementation.
 */
public class DefaultTimeTracker implements TimeTracker {

    @Override
    public List<WorklogDTO> list() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WorklogDTO> listByDay(final long timestamp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WorklogDTO> listByIssue(final long issueId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void newWorklog(final WorklogDTO worklogDTO) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeWorklog(final long worklogId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateWorklog(final WorklogDTO worklogDTO) {
        // TODO Auto-generated method stub

    }

}
