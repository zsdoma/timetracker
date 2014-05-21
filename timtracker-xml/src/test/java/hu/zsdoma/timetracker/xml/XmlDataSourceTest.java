package hu.zsdoma.timetracker.xml;

import static org.junit.Assert.fail;
import hu.zsdoma.timetracker.DefaultTimeTracker;
import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.schemas.ObjectFactory;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class XmlDataSourceTest {
    private hu.zsdoma.timetracker.api.TimeTracker timeTracker;
    private static ObjectFactory objectFactory;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        objectFactory = new ObjectFactory();
    }

    @Before
    public void setUp() throws Exception {
        timeTracker = new DefaultTimeTracker();
    }

    private TimeTrackerEntry generateTestTimeTrackerEntry() throws ParseException {
        Map<Long, WorklogEntry> worklogs = new HashMap<Long, WorklogEntry>();

        DateFormat dateFormat = DateUtils.dateFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");
        worklogs.put(begin.getTime(), worklog);

        begin = dateFormat.parse("2010.01.12. 03:50:00");
        end = dateFormat.parse("2010.01.12. 04:00:00");
        worklog = new WorklogEntry(begin, end, "worklog 2");
        worklogs.put(begin.getTime(), worklog);

        TimeTrackerEntry timeTrackerEntry = new TimeTrackerEntry(worklogs);
        return timeTrackerEntry;
    }

    @Test
    @Ignore
    public final void testSave() throws Exception {
        TimeTrackerEntry timeTrackerEntry = generateTestTimeTrackerEntry();

        File testXml = temporaryFolder.newFile("timetracker.xml");

        XmlDataSource xmlDataSource = new XmlDataSource(testXml);
        xmlDataSource.save(timeTrackerEntry);
    }

    @Test
    public final void testLoad() {
        fail("Not yet implemented"); // TODO
    }

}
