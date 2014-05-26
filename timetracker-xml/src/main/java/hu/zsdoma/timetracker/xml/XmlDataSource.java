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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * The {@link DataSource} JAXB implementation.
 */
public class XmlDataSource implements DataSource {
    /**
     * ObjectFactory for create JAXB elements.
     */
    private static final ObjectFactory objectFactory = new ObjectFactory();

    /**
     * The current timetracker database file.
     */
    private File file;

    /**
     * Constuctor with database file reference.
     * 
     * @param file
     *            {@link File} reference.
     */
    public XmlDataSource(File file) {
        super();
        checkFile(file);
        this.file = file;
    }

    /**
     * Check whether file is exists and not a directory.
     * 
     * @param file
     *            {@link File} reference.
     */
    private void checkFile(File file) {
        Objects.requireNonNull(file);
        if (file.isDirectory()) {
            throw new RuntimeException("Given file is directory!");
        }
    }

    /**
     * Save TimeTrackerEntry to a XML file.
     * 
     * @param database
     *            {@link TimeTrackerEntry}
     */
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

    /**
     * Open a file output stream by current file.
     * 
     * @return {@link FileOutputStream} reference.
     */
    private FileOutputStream openStream() {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileOutputStream;
    }

    /**
     * Build TimeTracker JAXB element from TimeTrackerEntry instance.
     * 
     * @param timeTrackerEntry
     *            {@link TimeTrackerEntry} reference.
     * 
     * @return {@link TimeTracker} reference.
     */
    private TimeTracker buildTimeTracker(TimeTrackerEntry timeTrackerEntry) {
        Map<Long, WorklogEntry> worklogEntries = timeTrackerEntry.getWorklogs();
        ObjectFactory objectFactory = new ObjectFactory();
        TimeTracker timeTracker = objectFactory.createTimeTracker();
        if (timeTrackerEntry.getCurrentWorklog() != null) {
            timeTracker.setCurrentWorklog(convertEntryToWorklog(timeTrackerEntry.getCurrentWorklog()));
        }
        for (WorklogEntry worklogEntry : worklogEntries.values()) {
            Worklog worklog = convertEntryToWorklog(worklogEntry);
            timeTracker.getWorklogs().add(worklog);
        }
        return timeTracker;
    }

    /**
     * Convert the given worklog entry to worklog JAXB element.
     * 
     * @param worklogEntry
     *            {@link WorklogEntry} reference.
     * @return {@link Worklog} reference.
     */
    private Worklog convertEntryToWorklog(WorklogEntry worklogEntry) {
        Worklog worklog = objectFactory.createWorklog();
        worklog.setId(worklogEntry.getId());
        worklog.setBeginTime(worklogEntry.getBeginTimestamp());
        worklog.setEndTime(worklogEntry.getEndTimeStamp());
        worklog.setMessage(worklogEntry.getMessage());
        return worklog;
    }

    /**
     * Load a timetracker database from current xml file.
     * 
     * @return {@link TimeTrackerEntry} reference.
     */
    @Override
    public TimeTrackerEntry load() {
        TimeTrackerEntry database;
        if (file.exists()) {
            TimeTracker timeTracker = null;
            try {
                timeTracker = loadTimeTracker();
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }

            Map<Long, WorklogEntry> worklogEntries = parseTimeTrackerElement(timeTracker.getWorklogs());
            WorklogEntry current = null;
            if (timeTracker.getCurrentWorklog() != null) {
                current = convertWorklogToEntry(timeTracker.getCurrentWorklog());
            }
            database = new TimeTrackerEntry(worklogEntries, current);
        } else {
            database = new TimeTrackerEntry();
        }
        return database;
    }

    /**
     * Parse worklog JAXB element list to worlog entry map.
     * 
     * @param worklogs
     *            List of {@link Worklog} reference.
     * @return Map of {@link WorklogEntry} by Worklog id.
     */
    private Map<Long, WorklogEntry> parseTimeTrackerElement(List<Worklog> worklogs) {
        Map<Long, WorklogEntry> worklogEntries = new HashMap<Long, WorklogEntry>();
        for (Worklog worklog : worklogs) {
            WorklogEntry worklogEntry = convertWorklogToEntry(worklog);
            worklogEntries.put(worklog.getId(), worklogEntry);
        }
        return worklogEntries;
    }

    /**
     * Convert a worklog JAXB element to worklog entry.
     * 
     * @param worklog
     *            {@link Worklog} reference.
     * @return {@link WorklogEntry} reference.
     */
    private WorklogEntry convertWorklogToEntry(Worklog worklog) {
        WorklogEntry worklogEntry = null;
        long begin = worklog.getBeginTime();
        long end = worklog.getEndTime();
        String message = worklog.getMessage();
        worklogEntry = new WorklogEntry(new Date(begin), new Date(end), message);
        return worklogEntry;
    }

    /**
     * Load a {@link TimeTracker} JAXB element from current file.
     * 
     * @return {@link TimeTracker} reference.
     * @throws JAXBException
     *             throw this if the JAXB unmarshalling failed.
     */
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

    /**
     * Open file input stream by current file.
     * 
     * @return {@link FileOutputStream} referece.
     */
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
