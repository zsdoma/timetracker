package hu.zsdoma.timetracker.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class TimeTrackerMain {
    
    private static CommandLineParser commandLineParser;

    /**
     * start
     * end
     * addEarlier
     * update
     * remove
     * listByDay
     * listAll
     * 
     * @param args
     */
    public static void main(String[] args) {
        commandLineParser = new GnuParser();

        Options options = new Options();
        options.addOption("s", "start", true, "Start worklog now.");
        options.addOption("e", "end", false, "End worklog now.");
        
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
