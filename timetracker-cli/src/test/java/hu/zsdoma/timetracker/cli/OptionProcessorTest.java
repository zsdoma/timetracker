package hu.zsdoma.timetracker.cli;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.utils.DateUtils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class OptionProcessorTest {

    private static CommandLineParser commandLineParser;
    private static Options options;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        commandLineParser = new GnuParser();
        options = OptionProcessor.buildOptions();
    }

    @Before
    public void setUp() throws Exception {
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

        }).when(timeTrackerMock).start(Mockito.anyString());
        // Mockito.verify(timeTrackerMock).startFrom(Mockito.any(Date.class), "hello");

        OptionProcessor optionProcessor = new OptionProcessor(commandLine, timeTrackerMock);
        optionProcessor.process();
    }

}
