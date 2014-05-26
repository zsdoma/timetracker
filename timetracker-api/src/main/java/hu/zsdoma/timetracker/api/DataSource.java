package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;

/**
 * Interface for timetracker save and load functionality.
 */
public interface DataSource {

    /**
     * Save a TimeTrackerEntry that contain all worklogs and the current worklog.
     * 
     * @param database
     *            {@link TimeTrackerEntry} reference.
     */
    void save(TimeTrackerEntry database);

    /**
     * Load {@link TimeTrackerEntry}.
     * 
     * @return {@link TimeTrackerEntry} reference.
     */
    TimeTrackerEntry load();
}
