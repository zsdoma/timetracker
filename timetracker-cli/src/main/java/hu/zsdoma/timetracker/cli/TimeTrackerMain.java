package hu.zsdoma.timetracker.cli;

import javax.xml.bind.annotation.XmlInlineBinaryData;

import hu.zsdoma.timetracker.DefaultTimeTracker;
import hu.zsdoma.timetracker.xml.XmlDataSource;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TimeTrackerMain {

    private static CommandLineParser commandLineParser;

    public static void main(String[] args) {
        commandLineParser = new GnuParser();

        Options options = OptionProcessor.buildOptions();

        try {
            CommandLine commandLine = commandLineParser.parse(options, args);

            if (commandLine.getOptions().length == 0 || commandLine.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("timetracker", options, true);
            } else {
                XmlDataSource xmlDataSource = new XmlDataSource(null);
                OptionProcessor optionsParser = new OptionProcessor(commandLine, new DefaultTimeTracker(xmlDataSource));
                optionsParser.process();
            }

        } catch (AlreadySelectedException e) {
            System.out.println("Choose only one from these options: s, e, l, u and r.");
        } catch (MissingArgumentException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
