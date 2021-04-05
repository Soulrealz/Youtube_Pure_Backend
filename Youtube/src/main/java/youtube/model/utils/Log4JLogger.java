package youtube.model.utils;

import org.apache.log4j.*;
import youtube.exceptions.FileNotFoundException;
import youtube.exceptions.IOException;

import java.io.*;

public class Log4JLogger {
    private static Logger logger;

    private Log4JLogger() {}

    public static Logger getLogger() {
        if (logger == null) {
            Appender appender = new Appender();
            logger = Logger.getLogger(Log4JLogger.class);
        }
        return logger;
    }

    private static class Appender {
        // date, priority, category, class name, message, newline
        // eg : 2021-04-05 02:28:14,051 [ERROR|youtube.model.utils.Log4JLogger|YoutubeApplication] msg
        private final String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        private FileAppender fileAppender;

        public Appender() {
            String path = getLogsPath();
            fileAppender = new FileAppender();
            Logger.getRootLogger().addAppender(fileAppender);

            fileAppender.setFile(path);
            fileAppender.setThreshold(Level.ALL);
            fileAppender.setLayout(new PatternLayout(PATTERN));
            fileAppender.activateOptions();
        }

        private String getLogsPath() {
            try {
                BufferedReader file = null;
                file = new BufferedReader(new FileReader("pathToLogs.txt"));
                return file.readLine();
            } catch (java.io.FileNotFoundException e) {
                Log4JLogger.getLogger().error("Logs.txt file not found.\n", e);
                throw new FileNotFoundException("File not found");
            } catch (java.io.IOException e) {
                Log4JLogger.getLogger().error("Critical IO exception.\n", e);
                throw new IOException("Bad Input/Output");
            }
        }
    }
}
