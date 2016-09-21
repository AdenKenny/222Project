package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import util.Logging.Levels;

/**
 * A class to log events in a .log file.
 *
 * @author Aden
 */

public final class Logging {

	/**
	 * Enums representing the levels of events being sent to the server.
	 *
	 * WARNING should represent an error that we can recover from.
	 * SEVERE should represent an error that we cannot recover from.
	 * EVENT should represent any other event such as user joining.
	 * OTHER should represent miscellaneous events.
	 */

	public enum Levels {
		WARNING,
		SEVERE,
		EVENT,
		OTHER; //Is this needed?
	}

	private Logging() {
		throw new AssertionError(); //Shouldn't be intitialised.
	}

	private static final File LOG_FILE = new File("logs/logs.log"); //The file we will log to.

	/**
	 * Logs an event to the log file, this event will contain the time it was logged at,
	 * the severity of the event, the class that the event was logged in, and a message
	 * that can be specified.
	 *
	 * @param className A string representing the name of the class that even was logged in.
	 * @param level An enum representing the severity of an event.
	 * @param message A string representing the message that will be logged.
	 */

	public static void logEvent(String className, Levels level, String message) {
		try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter)) { //Writer setup.

				printWriter.println(new LogEvent(level, className, message).out()); //Print the message to log.
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

	/**
	 * A class representing an event that will be logged to the output file.
	 *
	 * @author Aden
	 */

	class LogEvent{

		String level; //The level of the event.
		String className; //The class the event was registered in.
		String message; //The message to be logged.
		String timeStamp; //The time that the event was logged.

		/**
		 * Creates a LogEvent that can be outputed to the logging file. This LogEvent will
		 * contain the time that the event was logged, the severity of the event, the name
		 * of the class that the event was logged in, and a message that is specified.
		 *
		 * @param levelEnum The severity of the event.
		 * @param className A String representing the name of the origin class.
		 * @param message A string of the message to be logged.
		 */

		LogEvent(Levels levelEnum, String className, String message) {

			this.level = levelEnum.toString();
			this.className = className;
			this.message = message;
			this.timeStamp = new SimpleDateFormat("HH:mm:ss") .format(Calendar.getInstance().getTime());

		}

		/**
		 * Concatenates all the information and returns a full string that can then be logged.
		 *
		 * @return A string representing a LogEvent with all relevant information.
		 */

		String out() {
			String toLog = this.timeStamp + " - " + this.level + " in " + this.className + ": " + this.message;

			return toLog;
		}
	}


