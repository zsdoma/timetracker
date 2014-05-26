package hu.zsdoma.timetracker.tests;

import hu.zsdoma.timetracker.DefaultTimeTracker;
import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link DefaultTimeTracker}.
 */
public class DefaultTimeTrackerTest {

    /**
     * {@link TimeTracker} reference.
     */
    private TimeTracker timeTracker;

    /**
     * Create a new {@link DefaultTimeTracker} instance.
     */
    @Before
    public void setUp() {
        this.timeTracker = new DefaultTimeTracker();
    }

    /**
     * Test for start and end functionality.
     * 
     * @throws Exception
     *             exception that occurred during the test.
     */
    @Test
    public void testStartEnd() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.roll(Calendar.HOUR, false);
        timeTracker.startFrom(calendar.getTime(), "Start a test log.");

        WorklogEntry worklogEntry = timeTracker.current();
        Assert.assertEquals("Start a test log.", worklogEntry.getMessage());

        try {
            timeTracker.startFrom(new Date(calendar.getTimeInMillis() + 20000), "Start a test log, again");
            Assert.fail("Don't thronw TimeTrackerException.");
        } catch (TimeTrackerException e) {
        }

        timeTracker.end(new Date(), "Start a test log and end to.");
        worklogEntry = timeTracker.findById(calendar.getTimeInMillis());

        Assert.assertEquals("Start a test log and end to.", worklogEntry.getMessage());
    }

    /**
     * Tests for add earlier method.
     * 
     * @throws Exception
     *             exception that occurred during the test.
     */
    @Test
    public final void testAddEarlier() throws Exception {
        DateFormat dateFormat = DateUtils.dateFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");

        timeTracker.addEarlier(worklog);
        List<WorklogEntry> list = timeTracker.listAll();
        Assert.assertEquals(Arrays.asList(worklog), list);

        begin = dateFormat.parse("2010.01.12. 03:50:00");
        end = dateFormat.parse("2010.01.12. 04:00:00");
        WorklogEntry worklogDTO1 = new WorklogEntry(begin, end, "worklog 2");
        timeTracker.addEarlier(worklogDTO1);

        list = timeTracker.listAll();
        Assert.assertEquals(Arrays.asList(worklog, worklogDTO1), list);
    }

    /**
     * Tests for remove worklog functionality.
     * 
     * @throws Exception
     *             exception that occurred during the test.
     */
    @Test
    public void testRemove() throws Exception {
        DateFormat dateFormat = DateUtils.dateFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklogDTO = new WorklogEntry(begin, end, "worklog 1");

        timeTracker.addEarlier(worklogDTO);
        List<WorklogEntry> list = timeTracker.listAll();
        Assert.assertEquals(Arrays.asList(worklogDTO), list);

        timeTracker.removeById(worklogDTO.getId());

        list = timeTracker.listAll();
        Assert.assertTrue(list.isEmpty());
    }

    /**
     * Test for update worklog functionality.
     * 
     * @throws Exception
     *             exception that occurred during the test.
     */
    @Test
    public final void testUpdate() throws Exception {
        DateFormat dateFormat = DateUtils.dateFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        long id = begin.getTime();
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");
        timeTracker.addEarlier(worklog);

        begin = dateFormat.parse("2010.01.12. 03:50:00");
        end = dateFormat.parse("2010.01.12. 04:00:00");
        worklog = new WorklogEntry(begin, end, "worklog 2");
        timeTracker.addEarlier(worklog);

        List<WorklogEntry> list = timeTracker.listAll();
        Assert.assertEquals(2, list.size());

        worklog = timeTracker.findById(id);
        String testMessage = "new worklog message";
        worklog.setMessage(testMessage);
        timeTracker.update(worklog);

        list = timeTracker.listAll();
        Assert.assertEquals(list.get(0).getMessage(), testMessage);
    }

    /**
     * Test for time overlap checks, when the new worklog started earlier and ended later then a existed worklog ended.
     * 
     * @throws ParseException
     *             throwing during parse date strings.
     * @throws TimeTrackerException
     *             expected TimeTrackerException when overlap check is true.
     */
    @Test(expected = TimeTrackerException.class)
    public void testOverlapAfter() throws ParseException, TimeTrackerException {
        DateFormat dateFormat = DateUtils.dateFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");

        timeTracker.addEarlier(worklog);
        List<WorklogEntry> list = timeTracker.listAll();
        Assert.assertEquals(Arrays.asList(worklog), list);

        begin = dateFormat.parse("2010.01.12. 03:35:00");
        end = dateFormat.parse("2010.01.12. 04:00:00");
        WorklogEntry worklogDTO1 = new WorklogEntry(begin, end, "worklog 2");
        timeTracker.addEarlier(worklogDTO1);
    }

    /**
     * Test for time overlap checks, when the new worklog started earlier and ended later then a existed worklog
     * started.
     * 
     * @throws ParseException
     *             throwing during parse date strings.
     * @throws TimeTrackerException
     *             expected TimeTrackerException when overlap check is true.
     */
    @Test(expected = TimeTrackerException.class)
    public void testOverlapBefore() throws ParseException, TimeTrackerException {
        DateFormat dateFormat = DateUtils.dateFormatInstance();
        Date begin = dateFormat.parse("2010.01.12. 03:30:00");
        Date end = dateFormat.parse("2010.01.12. 03:40:00");
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");

        timeTracker.addEarlier(worklog);
        List<WorklogEntry> list = timeTracker.listAll();
        Assert.assertEquals(Arrays.asList(worklog), list);

        begin = dateFormat.parse("2010.01.12. 03:20:00");
        end = dateFormat.parse("2010.01.12. 03:31:00");
        WorklogEntry worklogDTO1 = new WorklogEntry(begin, end, "worklog 2");
        timeTracker.addEarlier(worklogDTO1);
    }

    /**
     * Test for start worklog in the future.
     * 
     * @throws TimeTrackerException
     *             expected {@link TimeTrackerException} then the start of worklog at in the future.
     */
    @Test(expected = TimeTrackerException.class)
    public void testFutureStart() throws TimeTrackerException {
        Date now = new Date();
        Date begin = new Date(now.getTime() + 20000);
        Date end = new Date(now.getTime() + 40000);
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");
        timeTracker.addEarlier(worklog);
    }

    /**
     * Test for end a worklog in the future.
     * 
     * @throws TimeTrackerException
     *             expected {@link TimeTrackerException} then the end of worklog at in the future.
     */
    @Test(expected = TimeTrackerException.class)
    public void testFutureEnd() throws TimeTrackerException {
        Date begin = new Date();
        Date end = new Date(begin.getTime() + 40000);
        WorklogEntry worklog = new WorklogEntry(begin, end, "worklog 1");
        timeTracker.addEarlier(worklog);
    }

}
