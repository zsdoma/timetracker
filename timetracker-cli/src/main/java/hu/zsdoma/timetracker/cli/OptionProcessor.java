package hu.zsdoma.timetracker.cli;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

public class OptionProcessor {

    public static final String OPTION_ID = "id";
    public static final String OPTION_DATE = "date";
    public static final String OPTION_MESSAGE = "message";
    public static final String OPTION_END_DATE = "endDate";
    public static final String OPTION_START_DATE = "startDate";
    private CommandLine commandLine;
    private TimeTracker timeTracker;

    public OptionProcessor(CommandLine commandLine, TimeTracker timeTracker) {
        this.commandLine = commandLine;
        this.timeTracker = timeTracker;
    }

    public void process() {
        try {
            if (commandLine.hasOption('s')) {
                processStartCase();
            }
            if (commandLine.hasOption('e')) {
                processEndCase();
            }
            if (commandLine.hasOption('l')) {
                System.out.println("list worklogs");
            }
            if (commandLine.hasOption('u')) {
                System.out.println("update worklog");
            }
            if (commandLine.hasOption('r')) {
                System.out.println("remove worklog");
            }
            if (commandLine.hasOption('a')) {
                System.out.println("add earlier worklog");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void processEndCase() throws ParseException {
        if (commandLine.hasOption(OPTION_END_DATE)) {
            Date endDate = processEndDate();
            Objects.requireNonNull(endDate, "The endDate options required for end option.");
            String message = processMessage();
            if (message == null) {
                timeTracker.end(endDate);
            } else {
                timeTracker.end(endDate, message);
            }
        } else {
            System.out.println("The endDate options required for end option.");
        }
    }

    private Date processEndDate() throws ParseException {
        return parseDate(commandLine.getOptionValue(OPTION_END_DATE));
    }

    private void processStartCase() throws ParseException {
        if (commandLine.hasOption(OPTION_MESSAGE)) {
            String message = processMessage();
            Date startDate = processStartDate();
            if (startDate == null) {
                timeTracker.start(message);
            } else {
                timeTracker.startFrom(startDate, message);
            }
        } else {
            System.out.println("Argument message is require for start option.");
        }
    }

    private String processMessage() {
        String message = commandLine.getOptionValue(OPTION_MESSAGE);
        return message;
    }

    private Date processStartDate() throws ParseException {
        Date startTime = null;
        if (commandLine.hasOption(OPTION_START_DATE)) {
            String startTimeString = commandLine.getOptionValue(OPTION_START_DATE);
            startTime = parseDate(startTimeString);
        }
        return startTime;
    }

    private Date parseDate(String startTimeString) throws ParseException {
        Date startTime = null;
        if (startTimeString != null) {
            startTime = DateUtils.dateFormatInstance().parse(startTimeString);
        }
        return startTime;
    }

    /*
     * start: [startDate], [startTime], message end: [startDate], [startTime], [endDate] ,[endTime], [message]
     * 
     * list: [date], [startDate, endDate], [startTime, endTime], [id] update: id, [startDate], [startTime], [endDate],
     * [endTime], [message] remove: id
     * 
     * addEarlier: [startDate], [startTime], [endDate], [endTime], [message]
     */
    @SuppressWarnings("static-access")
    public static Options buildOptions() {
        Option startDate = OptionBuilder.withArgName(OPTION_DATE)
                .withDescription("Start date (format: 2014.12.01. 12:24)").hasArg()
                .create(OPTION_START_DATE);
        Option endDate = OptionBuilder.withArgName(OPTION_DATE).hasArg()
                .withDescription("End date (format: 2014.12.01. 12:24)")
                .create(OPTION_END_DATE);
        Option date = OptionBuilder.withArgName(OPTION_DATE).hasArg()
                .withDescription("Date (format: 2014.12.01. 12:24)")
                .create(OPTION_DATE);
        Option message = OptionBuilder.withArgName(OPTION_MESSAGE).hasArg()
                .withDescription("Worklog message")
                .create(OPTION_MESSAGE);
        Option id = OptionBuilder.withArgName(OPTION_ID).hasArg()
                .withDescription("Worklog id. Only for 'list' and 'remove' case.")
                .create(OPTION_ID);

        Option start = new Option("s", "start", false, "Start worklog now.");
        Option end = new Option("e", "end", false, "End worklog now.");
        Option list = new Option("l", "list", false, "List all worklogs.");
        Option update = new Option("u", "update", false, "Update worklog.");
        Option remove = new Option("r", "remove", false, "Remove worklog by id.");
        Option addEarlier = new Option("a", "add-earlier", false, "Add earlier worklog.");

        OptionGroup commands = new OptionGroup();
        commands.isRequired();
        commands.addOption(start);
        commands.addOption(end);
        commands.addOption(list);
        commands.addOption(update);
        commands.addOption(remove);
        commands.addOption(addEarlier);

        Options options = new Options();
        options.addOptionGroup(commands);
        options.addOption("h", "help", false, "Show help");
        options.addOption(id).addOption(message).addOption(date).addOption(startDate).addOption(endDate);
        return options;
    }

}
