package hu.zsdoma.timetracker.api;

import hu.zsdoma.timetracker.api.dto.Database;

public interface DataSource {

    void save(Database database);

    Database load();
}
