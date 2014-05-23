package hu.zsdoma.timetracker.cli;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TimeTrackerMain {

    private static CommandLineParser commandLineParser;

    /**
     * start end addEarlier update remove listByDay listAll
     * 
     * @param args
     */
    public static void main(String[] args) {
        commandLineParser = new GnuParser();

        Options options = buildOptions();

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0 || commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("timetracker", options, true);
            } else {
                processCommandLine(commandLine);
            }

        } catch (AlreadySelectedException e) {
            System.out.println("Choose only one from these options: s, e, l, u and r.");
        } catch (MissingArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processCommandLine(CommandLine commandLine) {
        if (commandLine.hasOption('s')) {
            System.out.println("start worklog");
        }
        if (commandLine.hasOption('e')) {
            System.out.println("end worklog");
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
    }

    /*
     * start: [startDate], [startTime], message end: [startDate], [startTime], [endDate] ,[endTime], [message]
     * 
     * list: [date], [startDate, endDate], [startTime, endTime], [id] update: id, [startDate], [startTime], [endDate],
     * [endTime], [message] remove: id
     * 
     * addEarlier: [startDate], [startTime], [endDate], [endTime], [message]
     */
    private static Options buildOptions() {
        Option startDate = OptionBuilder.withArgName("date")
                .withDescription("Start date (format: 2013.12.12.)").hasArg()
                .create("startDate");
        Option startTime = OptionBuilder.withArgName("time").hasArg()
                .withDescription("Start time (format: 12:12)")
                .create("startTime");
        Option endDate = OptionBuilder.withArgName("date").hasArg()
                .withDescription("End date (format: 2013.12.12.)")
                .create("endDate");
        Option endTime = OptionBuilder.withArgName("time").hasArg()
                .withDescription("End date (format: 12:12)")
                .create("endTime");
        Option date = OptionBuilder.withArgName("date").hasArg()
                .withDescription("Date (format: 2013.12.12.)")
                .create("date");
        Option message = OptionBuilder.withArgName("message").hasArg()
                .withDescription("Worklog message")
                .create("message");
        Option id = OptionBuilder.withArgName("id").hasArg()
                .withDescription("Worklog id. Only for 'list' and 'remove' case.")
                .create("id");

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
        options.addOption(id).addOption(message).addOption(date).addOption(startTime).addOption(endTime)
                .addOption(startDate).addOption(endDate);
        return options;
    }

}
