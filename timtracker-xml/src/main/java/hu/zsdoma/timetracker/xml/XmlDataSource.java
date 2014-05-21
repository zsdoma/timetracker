package hu.zsdoma.timetracker.xml;

import hu.zsdoma.timetracker.api.DataSource;
import hu.zsdoma.timetracker.api.dto.Database;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;
import hu.zsdoma.timetracker.schemas.ObjectFactory;
import hu.zsdoma.timetracker.schemas.TimeTracker;
import hu.zsdoma.timetracker.schemas.Worklog;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Node;

public class XmlDataSource implements DataSource {

    private OutputStream outputStream;
    private InputStream inputStream;

    public XmlDataSource(OutputStream outputStream) {
        super();
        this.outputStream = outputStream;
    }

    @Override
    public void save(Database database) {
        TimeTracker timeTracker = buildTimeTracker(database);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TimeTracker.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.marshal(timeTracker, outputStream);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private TimeTracker buildTimeTracker(Database database) {
        Map<Long, WorklogEntry> worklogEntries = database.getWorklogs();
        ObjectFactory objectFactory = new ObjectFactory();
        TimeTracker timeTracker = objectFactory.createTimeTracker();

        for (WorklogEntry worklogEntry : worklogEntries.values()) {
            Worklog worklog = objectFactory.createWorklog();
            worklog.setId(worklogEntry.getId());
            worklog.setBeginTime(worklogEntry.getBeginTimestamp());
            worklog.setEndTime(worklogEntry.getEndTimeStamp());
            worklog.setMessage(worklog.getMessage());
            timeTracker.getWorklogs().add(worklog);
        }
        return timeTracker;
    }

    @Override
    public Database load() {
        TimeTracker timeTracker = null;
        try {
            timeTracker = loadTimeTracker();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        List<Worklog> worklogs = timeTracker.getWorklogs();

        Map<Long, WorklogEntry> worklogEntries = new HashMap<Long, WorklogEntry>();
        for (Worklog worklog : worklogs) {
            long begin = worklog.getBeginTime();
            long end = worklog.getEndTime();
            String message = worklog.getMessage();
            WorklogEntry worklogEntry = new WorklogEntry(new Date(begin), new Date(end), message);
            worklogEntries.put(worklog.getId(), worklogEntry);
        }

        Database database = new Database(worklogEntries);

        return database;
    }

    private TimeTracker loadTimeTracker() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TimeTracker.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object object = unmarshaller.unmarshal(inputStream);
        if (object instanceof TimeTracker) {
            return (TimeTracker) object;
        } else {
            throw new TimeTrackerException("Can't find TimeTracker element in xml.");
        }
    }

}
