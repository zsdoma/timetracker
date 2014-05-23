package hu.zsdoma.timetracker.cli;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class OptionProcessorTest {

    private static CommandLineParser commandLineParser;
    private static Options options;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        options = OptionProcessor.buildOptions();
        commandLineParser = new GnuParser();
    }

    @Test
    public final void testStart() throws Exception {
        String[] args = new String[] { "-s", "-message", "hello" };

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(1, invocation.getArguments().length);

                String message = (String) invocation.getArguments()[0];
                Assert.assertEquals("hello", message);
                return null;
            }

        }).when(timeTrackerMock).start(Mockito.anyString());

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).start(Mockito.anyString());
    }

    @Test
    public void testStartStartDate() throws Exception {
        final String startDate = "2014.01.12. 13:13";
        String[] args = new String[] { "-s", "-message", "hello", "-startDate", startDate };

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(2, invocation.getArguments().length);

                Date date = (Date) invocation.getArguments()[0];
                Assert.assertEquals(DateUtils.dateFormatInstance().parse(startDate), date);

                String message = (String) invocation.getArguments()[1];
                Assert.assertEquals("hello", message);
                return null;
            }

        }).when(timeTrackerMock).startFrom(Mockito.any(Date.class), Mockito.anyString());

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).startFrom(Mockito.any(Date.class), Mockito.anyString());
    }

    @Test
    public final void testEndMessage() throws Exception {
        final String endDate = "2014.01.12. 13:13";
        String[] args = new String[] { "-e", "-endDate", endDate, "-message", "hello" };

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(2, invocation.getArguments().length);

                Date date = (Date) invocation.getArguments()[0];
                Assert.assertEquals(DateUtils.dateFormatInstance().parse(endDate), date);

                String message = (String) invocation.getArguments()[1];
                Assert.assertEquals("hello", message);
                return null;
            }

        }).when(timeTrackerMock).end(Mockito.any(Date.class), Mockito.anyString());

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).end(Mockito.any(Date.class), Mockito.anyString());
    }

    @Test
    public final void testEnd() throws Exception {
        final String endDate = "2014.01.12. 13:13";
        String[] args = new String[] { "-e", "-endDate", endDate };

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(1, invocation.getArguments().length);

                Date date = (Date) invocation.getArguments()[0];
                Assert.assertEquals(DateUtils.dateFormatInstance().parse(endDate), date);
                return null;
            }

        }).when(timeTrackerMock).end(Mockito.any(Date.class));

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).end(Mockito.any(Date.class));
    }

    @Test
    public final void testRemove() throws Exception {
        String[] args = new String[] { "-r", "-id", "1000" };

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(1, invocation.getArguments().length);

                long id = (long) invocation.getArguments()[0];
                Assert.assertEquals(1000, id);
                return null;
            }

        }).when(timeTrackerMock).removeById(Mockito.anyLong());

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).removeById(Mockito.anyLong());
    }

    @Test
    public final void testAddEarlier() throws Exception {
        final String startString = "2014.01.12. 13:13";
        final String endString = "2014.01.12. 13:23";
        String message = "hello";
        String[] args = new String[] { "-a", "-startDate", startString, "-endDate", endString, "-message", message };

        final WorklogEntry expectedWorklogEntry = new WorklogEntry(DateUtils.dateFormatInstance().parse(startString),
                DateUtils.dateFormatInstance().parse(endString), message);

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(1, invocation.getArguments().length);

                WorklogEntry worklogEntry = (WorklogEntry) invocation.getArguments()[0];
                Assert.assertEquals(expectedWorklogEntry, worklogEntry);
                return null;
            }

        }).when(timeTrackerMock).addEarlier(Mockito.any(WorklogEntry.class));

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).addEarlier(Mockito.any(WorklogEntry.class));
    }

    @Test
    public final void testUpdate() throws Exception {
        final String startString = "2014.01.12. 13:13";
        final String endString = "2014.01.12. 13:23";
        String message = "hello";
        String[] args = new String[] { "--update", "-id", "1000", "-startDate", startString, "-endDate", endString, "-message", message };

        final WorklogEntry expectedWorklogEntry = new WorklogEntry(DateUtils.dateFormatInstance().parse(startString),
                DateUtils.dateFormatInstance().parse(endString), message);

        CommandLine commandLine = commandLineParser.parse(options, args);

        TimeTracker timeTrackerMock = Mockito.mock(TimeTracker.class);
        Mockito.doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Assert.assertNotNull(invocation);
                Assert.assertNotNull(invocation.getArguments());
                Assert.assertEquals(1, invocation.getArguments().length);

                WorklogEntry worklogEntry = (WorklogEntry) invocation.getArguments()[0];
                Assert.assertEquals(expectedWorklogEntry, worklogEntry);
                return null;
            }

        }).when(timeTrackerMock).update(Mockito.any(WorklogEntry.class));

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();

        Mockito.verify(timeTrackerMock, Mockito.only()).update(Mockito.any(WorklogEntry.class));
    }

}
