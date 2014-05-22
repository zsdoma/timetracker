package hu.zsdoma.timetracker.xml;

import hu.zsdoma.timetracker.api.DataSource;
import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;
import hu.zsdoma.timetracker.schemas.ObjectFactory;
import hu.zsdoma.timetracker.schemas.TimeTracker;
import hu.zsdoma.timetracker.schemas.Worklog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlDataSource implements DataSource {

    private File file;

    public XmlDataSource(File file) {
        super();
        this.file = file;
    }

    @Override
    public void save(TimeTrackerEntry database) {
        TimeTracker timeTracker = buildTimeTracker(database);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName(),
                    ObjectFactory.class.getClassLoader());
            // TODO fix formatted output
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(timeTracker, openStream());
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private FileOutputStream openStream() {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileOutputStream;
    }

    private TimeTracker buildTimeTracker(TimeTrackerEntry database) {
        Map<Long, WorklogEntry> worklogEntries = database.getWorklogs();
        ObjectFactory objectFactory = new ObjectFactory();
        TimeTracker timeTracker = objectFactory.createTimeTracker();

        for (WorklogEntry worklogEntry : worklogEntries.values()) {
            Worklog worklog = objectFactory.createWorklog();
            worklog.setId(worklogEntry.getId());
            worklog.setBeginTime(worklogEntry.getBeginTimestamp());
            worklog.setEndTime(worklogEntry.getEndTimeStamp());
            worklog.setMessage(worklogEntry.getMessage());
            timeTracker.getWorklogs().add(worklog);
        }
        return timeTracker;
    }

    @Override
    public TimeTrackerEntry load() {
        TimeTracker timeTracker = null;
        try {
            timeTracker = loadTimeTracker();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        Map<Long, WorklogEntry> worklogEntries = parseTimeTrackerElement(timeTracker);

        TimeTrackerEntry database = new TimeTrackerEntry(worklogEntries);
        return database;
    }

    private Map<Long, WorklogEntry> parseTimeTrackerElement(TimeTracker timeTracker) {
        List<Worklog> worklogs = timeTracker.getWorklogs();

        Map<Long, WorklogEntry> worklogEntries = new HashMap<Long, WorklogEntry>();
        for (Worklog worklog : worklogs) {
            long begin = worklog.getBeginTime();
            long end = worklog.getEndTime();
            String message = worklog.getMessage();
            WorklogEntry worklogEntry = new WorklogEntry(new Date(begin), new Date(end), message);
            worklogEntries.put(worklog.getId(), worklogEntry);
        }
        return worklogEntries;
    }

    private TimeTracker loadTimeTracker() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(TimeTracker.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Object object = unmarshaller.unmarshal(openInputStream());
        if (object instanceof TimeTracker) {
            return (TimeTracker) object;
        } else {
            throw new TimeTrackerException("Can't find TimeTracker element in xml.");
        }
    }

    private FileInputStream openInputStream() {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileInputStream;
    }

}
