package hu.zsdoma.timetracker.tests.xml;

import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.utils.DateUtils;
import hu.zsdoma.timetracker.xml.XmlDataSource;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for {@link XmlDataSource}.
 */
public class XmlDataSourceTest {
    /**
     * Temporary folder.
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Test save and load {@link XmlDataSource}.
     * 
     * @throws Exception
     *             exception that occurred during the test.
     */
    @Test
    public final void testSave() throws Exception {
        TimeTrackerEntry timeTrackerEntry = generateTimeTrackerEntry();

        File testXml = temporaryFolder.newFile("timetracker.xml");

        XmlDataSource xmlDataSource = new XmlDataSource(testXml);
        xmlDataSource.save(timeTrackerEntry);

        TimeTrackerEntry loadedTimeTrackerEntry = xmlDataSource.load();
        Assert.assertEquals(loadedTimeTrackerEntry, timeTrackerEntry);
    }

    /**
     * Generate test time tracker entry.
     * 
     * @return {@link TimeTrackerEntry} reference.
     * @throws ParseException
     *             throw this when the date format wrong.
     */
    private TimeTrackerEntry generateTimeTrackerEntry() throws ParseException {
        Map<Long, WorklogEntry> worklogs = new HashMap<Long, WorklogEntry>();
        DateFormat dateFormat = DateUtils.dateTimeFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");
        worklogs.put(begin.getTime(), worklog);

        begin = dateFormat.parse("2010.01.12. 03:50:00");
        end = dateFormat.parse("2010.01.12. 04:00:00");
        worklog = new WorklogEntry(begin, end, "worklog 2");
        worklogs.put(begin.getTime(), worklog);

        WorklogEntry currentWorklogEntry = new WorklogEntry(dateFormat.parse("2010.01.14. 05:10"), "current worklog");
        return new TimeTrackerEntry(worklogs, currentWorklogEntry);
    }

}
