package hu.zsdoma.timetracker.cli;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
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
                throw new UnsupportedOperationException("Not implemented, yet.");
            }
            if (commandLine.hasOption('u')) {
                processUpdateCase();
            }
            if (commandLine.hasOption('r')) {
                processRemmoveCase();
            }
            if (commandLine.hasOption('a')) {
                processAddEarlierCase();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void processUpdateCase() throws ParseException {
        if (commandLine.hasOption(OPTION_ID)) {
            Date startDate = processStartDate(false);
            Date endDate = processEndDate(false);
            String message = processMessage();
            WorklogEntry worklogEntry = new WorklogEntry(startDate, endDate, message);
            timeTracker.update(worklogEntry);
        } else {
            String missingOptionMessage = "The id option is required for update option.";
            throwMissingOptionException(missingOptionMessage);
        }
    }

    private void throwMissingOptionException(String missingOptionMessage) {
        throw new RuntimeException(new MissingArgumentException(missingOptionMessage));
    }

    private void processAddEarlierCase() throws ParseException {
        if (hasOptionsForAddEarlier()) {
            String message = processMessage();
            Date startDate = processStartDate(true);
            Date endDate = processEndDate(true);
            WorklogEntry worklogEntry = new WorklogEntry(startDate, endDate, message);
            timeTracker.addEarlier(worklogEntry);
        } else {
            throwMissingOptionException("The startDate, endDate, message is required for addEarlier option.");
        }
    }

    private boolean hasOptionsForAddEarlier() {
        return commandLine.hasOption(OPTION_START_DATE) && commandLine.hasOption(OPTION_END_DATE)
                && commandLine.hasOption(OPTION_MESSAGE);
    }

    private void processRemmoveCase() {
        if (commandLine.hasOption(OPTION_ID)) {
            Long id = processId(true);
            timeTracker.removeById(id);
        }
    }

    // FIXME require
    private Long processId(boolean require) {
        Long id = null;
        try {
            id = Long.parseLong(commandLine.getOptionValue(OPTION_ID));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    private void processEndCase() throws ParseException {
        if (commandLine.hasOption(OPTION_END_DATE)) {
            Date endDate = processEndDate(true);
            String message = processMessage();
            if (message == null) {
                timeTracker.end(endDate);
            } else {
                timeTracker.end(endDate, message);
            }
        } else {
            throwMissingOptionException("The endDate options required for end option.");
        }
    }

    private Date processEndDate(boolean require) throws ParseException {
        Date endDate = null;
        if (commandLine.hasOption(OPTION_END_DATE)) {
            endDate = parseDate(commandLine.getOptionValue(OPTION_END_DATE));
        }
        if (require && endDate == null) {
            throwMissingOptionException("The endDate is required for this option.");
        }
        return endDate;
    }

    private void processStartCase() throws ParseException {
        if (commandLine.hasOption(OPTION_MESSAGE)) {
            String message = processMessage();
            Date startDate = processStartDate(false);
            if (startDate == null) {
                timeTracker.start(message);
            } else {
                timeTracker.startFrom(startDate, message);
            }
        } else {
            throwMissingOptionException("Argument message is require for start option.");
        }
    }

    private String processMessage() {
        String message = commandLine.getOptionValue(OPTION_MESSAGE);
        return message;
    }

    private Date processStartDate(boolean required) throws ParseException {
        Date startDate = null;
        if (commandLine.hasOption(OPTION_START_DATE)) {
            startDate = parseDate(commandLine.getOptionValue(OPTION_START_DATE));
        }
        if (required && startDate == null) {
            throwMissingOptionException("The startDate is required for this option.");
        }
        return startDate;
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
