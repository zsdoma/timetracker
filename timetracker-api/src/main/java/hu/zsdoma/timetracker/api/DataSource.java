package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;

public interface DataSource {

    void save(TimeTrackerEntry database);

    TimeTrackerEntry load();
}
