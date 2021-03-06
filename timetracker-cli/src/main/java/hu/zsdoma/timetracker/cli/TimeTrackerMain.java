package hu.zsdoma.timetracker.cli;

import hu.zsdoma.timetracker.DefaultTimeTracker;
import hu.zsdoma.timetracker.api.exception.TimeTrackerException;
import hu.zsdoma.timetracker.xml.XmlDataSource;

import java.io.File;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for TimeTracker CLI.
 */
public class TimeTrackerMain {

    /**
     * TimeTracker database and settings folder in user home.
     */
    private static final String TIMETRACKER_FOLDER = ".timetracker";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTrackerMain.class);

    /**
     * Default database xml file name.
     */
    private static final String DATABASE_XML = "timetracker.xml";

    /**
     * {@link CommandLineParser} to parse command line arguments.
     */
    private static CommandLineParser commandLineParser;

    /**
     * Main method.
     * 
     * @param args
     *            command line args.
     */
    public static void main(String[] args) {
        File xmlFile = getOrCreateXmlFile();

        commandLineParser = new GnuParser();

        Options options = OptionProcessor.buildOptions();

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0 || commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("timetracker", options, true);
            } else {
                XmlDataSource xmlDataSource = new XmlDataSource(xmlFile);
                DefaultTimeTracker defaultTimeTracker = new DefaultTimeTracker(xmlDataSource);
                OptionProcessor optionsProcessor = new OptionProcessor(commandLine, defaultTimeTracker);
                optionsProcessor.process();
            }
        } catch (AlreadySelectedException e) {
            LOGGER.error("Choose only one from these options: s, e, l, u and r.");
        } catch (MissingArgumentException e) {
            LOGGER.error(e.getMessage());
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        } catch (TimeTrackerException e) {
            LOGGER.error(e.getMessage());
        } catch (java.text.ParseException e) {
            LOGGER.error(e.getMessage());
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Get XML database file (or create if not exists) from user.home/.timetracker folder.
     * 
     * @return {@link File} reference.
     */
    private static File getOrCreateXmlFile() {
        String userHomeFolder = System.getProperty("user.home");
        String xmlPath = userHomeFolder + File.separator + TIMETRACKER_FOLDER;
        File timetrackerFolder = new File(xmlPath);
        if (!timetrackerFolder.exists()) {
            if (!timetrackerFolder.mkdir()) {
                throw new TimeTrackerException("Can't create .timetracker folder in user home.");
            }
        }
        return new File(xmlPath + File.separator + DATABASE_XML);
    }

}
