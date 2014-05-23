package hu.zsdoma.timetracker.xml;

import hu.zsdoma.timetracker.DefaultTimeTracker;
import hu.zsdoma.timetracker.api.dto.TimeTrackerEntry;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.schemas.ObjectFactory;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
        return generateTimeTrackerEntry();
    }

    @Test
    public final void testSave() throws Exception {
        TimeTrackerEntry timeTrackerEntry = generateTestTimeTrackerEntry();

        File testXml = temporaryFolder.newFile("timetracker.xml");

        XmlDataSource xmlDataSource = new XmlDataSource(testXml);
        xmlDataSource.save(timeTrackerEntry);

        String xmlString = loadTestFile(testXml);
        Assert.assertEquals(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><timeTracker xmlns=\"http://www.example.org/worklog\">    <worklogs>        <id>1263264600000</id>        <message>worklog 2</message>        <beginTime>1263264600000</beginTime>        <endTime>1263265200000</endTime>    </worklogs>    <worklogs>        <id>1263263400000</id>        <message>worklog 1</message>        <beginTime>1263263400000</beginTime>        <endTime>1263264000000</endTime>    </worklogs></timeTracker>",
                xmlString);
    }

    private String loadTestFile(File testXml) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(testXml))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }

    @Test
    public final void testLoad() throws Exception {
        URI testFileURI = getClass().getResource("/test-timetracker.xml").toURI();
        File testXml = new File(testFileURI);

        XmlDataSource xmlDataSource = new XmlDataSource(testXml);
        TimeTrackerEntry timeTrackerEntry = xmlDataSource.load();

        TimeTrackerEntry expectedTimeTrackerEntry = generateTimeTrackerEntry();

        Assert.assertEquals(expectedTimeTrackerEntry, timeTrackerEntry);
    }

    private TimeTrackerEntry generateTimeTrackerEntry() throws ParseException {
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
        return new TimeTrackerEntry(worklogs, null);
    }

}
