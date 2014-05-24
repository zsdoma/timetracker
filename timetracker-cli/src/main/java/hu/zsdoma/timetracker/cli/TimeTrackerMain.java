package hu.zsdoma.timetracker.cli;

import hu.zsdoma.timetracker.DefaultTimeTracker;
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

public class TimeTrackerMain {
    
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

    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");
        File xmlFile = getOrCreateXmlFile(userHome);

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
            System.out.println("Choose only one from these options: s, e, l, u and r.");
        } catch (MissingArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getOrCreateXmlFile(String userHome) {
        return new File(userHome + File.separator + DATABASE_XML);
    }

}
