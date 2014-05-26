package hu.zsdoma.timetracker.cli;

import hu.zsdoma.timetracker.api.TimeTracker;
import hu.zsdoma.timetracker.api.dto.WorklogEntry;
import hu.zsdoma.timetracker.utils.DateUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;

/**
 * Process command line options and call the applicable timetracker function.
 */
public class OptionProcessor {
    /**
     * Id option string.
     */
    public static final String OPTION_ID = "id";

    /**
     * Date option string for list worklogs option.
     */
    public static final String OPTION_DATE = "date";

    /**
     * Message option string.
     */
    public static final String OPTION_MESSAGE = "message";

    /**
     * End date option string.
     */
    public static final String OPTION_END_DATE = "endDate";

    /**
     * Start date option string.
     */
    public static final String OPTION_START_DATE = "startDate";

    /**
     * Parsed {@link CommandLine} reference.
     */
    private CommandLine commandLine;

    /**
     * {@link TimeTracker} reference.
     */
    private TimeTracker timeTracker;

    /**
     * Constuctor with {@link CommandLine} and {@link TimeTracker} reference.
     * 
     * @param commandLine
     *            CommandLine that contain the given command line options.
     * @param timeTracker
     *            {@link TimeTracker} reference.
     */
    public OptionProcessor(CommandLine commandLine, TimeTracker timeTracker) {
        this.commandLine = commandLine;
        this.timeTracker = timeTracker;
    }

    /**
     * Process the given command line.
     */
    public void process() {
        try {
            if (commandLine.hasOption('s')) {
                processStartCase();
            }
            if (commandLine.hasOption('e')) {
                processEndCase();
            }
            if (commandLine.hasOption('l')) {
                processListCase();
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

    /**
     * List case implementation.
     * 
     * @throws ParseException
     *             throw this when the date format wrong.
     */
    private void processListCase() throws ParseException {
        Date date = processDate();
        List<WorklogEntry> worklogs = Collections.emptyList();
        if (date != null) {
            worklogs = timeTracker.listByDay(date.getTime());
        } else {
            worklogs = timeTracker.list();
        }
        if (worklogs != null) {
            showWorklogs(worklogs);
        }
    }

    /**
     * Write formatted worklog list to standard output.
     * 
     * @param worklogs
     *            List of {@link WorklogEntry}.
     */
    private void showWorklogs(List<WorklogEntry> worklogs) {
        String leftAlignFormat = "| %-13d | %-17s | %-17s | %-25s |%n";

        System.out.format("+---------------+-------------------+-------------------+---------------------------+%n");
        System.out.printf("| Id            | Start Date        | End Date          | Message                   |%n");
        System.out.format("+---------------+-------------------+-------------------+---------------------------+%n");
        for (WorklogEntry worklogEntry : worklogs) {
            String start = DateUtils.dateFormatInstance().format(new Date(worklogEntry.getBeginTimestamp()));
            String end = DateUtils.dateFormatInstance().format(new Date(worklogEntry.getEndTimeStamp()));
            System.out.format(leftAlignFormat, worklogEntry.getId(), start, end, worklogEntry.getMessage());
        }
        System.out.format("+---------------+-------------------+-------------------+---------------------------+%n");
    }

    /**
     * Parse date option from {@link CommandLine}.
     * 
     * @return value of date option in {@link Date} representation.
     * @throws ParseException
     *             throw this when the date format wrong.
     */
    private Date processDate() throws ParseException {
        Date date = null;
        if (commandLine.hasOption(OPTION_DATE)) {
            date = parseDate(commandLine.getOptionValue(OPTION_DATE));
        }
        return date;
    }

    /**
     * Process the update case.
     * 
     * @throws ParseException
     *             throw this when the date format wrong.
     */
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

    /**
     * Throw a {@link MissingOptionException} with the given message.
     * 
     * @param missingOptionMessage
     *            Exception message.
     */
    private void throwMissingOptionException(String missingOptionMessage) {
        throw new RuntimeException(new MissingArgumentException(missingOptionMessage));
    }

    /**
     * Add earlier case implementation.
     * 
     * @throws ParseException
     *             throw this when the date format wrong.
     */
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

    /**
     * Check whether all required option is exists.
     * 
     * @return <code>true</code> if all option exists, <code>false</code> otherwise.
     */
    private boolean hasOptionsForAddEarlier() {
        return commandLine.hasOption(OPTION_START_DATE) && commandLine.hasOption(OPTION_END_DATE)
                && commandLine.hasOption(OPTION_MESSAGE);
    }

    /**
     * Remove worklog case implementation.
     */
    private void processRemmoveCase() {
        if (commandLine.hasOption(OPTION_ID)) {
            Long id = processId();
            if (id == null) {
                throwMissingOptionException("The id option is required.");
            }
            timeTracker.removeById(id);
        }
    }

    /**
     * Process id param from {@link CommandLine}.
     * 
     * @return Parsed process id or null.
     */
    private Long processId() {
        Long id = null;
        try {
            id = Long.parseLong(commandLine.getOptionValue(OPTION_ID));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

    /**
     * End worklog case implementation.
     * 
     * @throws ParseException
     *             throw this when the date format wrong.
     */
    private void processEndCase() throws ParseException {
        timeTracker.end(processEndDate(false), processMessage());
    }

    /**
     * Process end date from {@link CommandLine}.
     * 
     * @param require
     *            if <code>true</code> then endDate option is required. If <code>true</code> and missing then throw
     *            {@link MissingOptionException}.
     * @return value of endDate option or null.
     * @throws ParseException
     *             throw this when the date format wrong.
     */
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

    /**
     * Start case implementation.
     * 
     * @throws ParseException
     *             throw this when the date format wrong.
     */
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

    /**
     * Process message from {@link CommandLine}.
     * 
     * @return Value of message option or null.
     */
    private String processMessage() {
        String message = commandLine.getOptionValue(OPTION_MESSAGE);
        return message;
    }

    /**
     * Process start date option from {@link CommandLine}.
     * 
     * @param required
     *            If <code>true</code> then required option and throw {@link MissingOptionException} when missing.
     * @return value of start date option in {@link Date} representation.
     * @throws ParseException
     *             throw this when the date format wrong.
     */
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

    /**
     * Parse date string to {@link Date} instance. Use {@link DateUtils#DATE_FORMAT} pattern.
     * 
     * @param dateString
     *            date string.
     * @return {@link Date} instance or null.
     * @throws ParseException
     *             throw this when the date format wrong.
     */
    private Date parseDate(String dateString) throws ParseException {
        Date startTime = null;
        if (dateString != null) {
            startTime = DateUtils.dateFormatInstance().parse(dateString);
        }
        return startTime;
    }

    /**
     * Build {@link Options} instance for command line configuration.
     * 
     * @return {@link Options} instance.
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
