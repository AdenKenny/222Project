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
	 * Enums representing the levels of events being sent to the server. SEVERE should represent an
	 * error that we cannot recover from. WARNING should represent an error that we can recover
	 * from. EVENT should represent any other event such as user joining. OTHER should represent
	 * miscellaneous events.
	 */

	public enum Levels {
		SEVERE(0), WARNING(1), EVENT(2), OTHER(3); // Is this needed?

		final int value;

		Levels(int value) {
			this.value = value;
		}
	}

	private Logging() {
		throw new AssertionError(); // Shouldn't be intitialised.
	}

	private static final File LOG_FILE = new File("logs/logs.log"); // The file we will log to.

	/**
	 * Logs an event to the log file, this event will contain the time it was logged at, the
	 * severity of the event, the class that the event was logged in, and a message that can be
	 * specified.
	 *
	 * @param className
	 *            A string representing the name of the class that even was logged in.
	 * @param level
	 *            An enum representing the severity of an event.
	 * @param message
	 *            A string representing the message that will be logged.
	 */

	public static void logEvent(String className, Levels level, String message) {
		try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter)) { // Writer setup.

			LogEvent event = new LogEvent(className, level, message);

			String eventOut = event.out();

			printWriter.println(eventOut); // Print the message to log.
			
			System.out.println(eventOut);

		}

		catch (IOException e) {
			e.printStackTrace(); // Was about to log errors with the logger in the logger...
		}
	}
}

/**
 * A class representing an event that will be logged to the output file. Note: this class has a
 * natural ordering that is inconsistent with equals.
 *
 * @author Aden
 */

class LogEvent implements Comparable<LogEvent> {

	Levels level; // The level of the event.
	String message; // The message to be logged.
	String timeStamp; // The time that the event was logged.
	String className;

	/**
	 * Creates a LogEvent that can be outputed to the logging file. This LogEvent will contain the
	 * time that the event was logged, the severity of the event, the name of the class that the
	 * event was logged in, and a message that is specified.
	 *
	 * @param levelEnum
	 *            The severity of the event.
	 * @param className
	 *            A String representing the name of the origin class.
	 * @param message
	 *            A string of the message to be logged.
	 */

	LogEvent(String className, Levels levelEnum, String message) {

		this.level = levelEnum;
		this.className = className;
		this.message = message;
		this.timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	}

	/**
	 * Concatenates all the information and returns a full string that can then be logged.
	 *
	 * @return A string representing a LogEvent with all relevant information.
	 */

	String out() {
		String toLog = this.timeStamp + " - " + this.level.toString() + " in " + this.className + ": " + this.message;
		return toLog;
	}

	/**
	 * Compares a log event to another by severity. The more severe an event, the greater the
	 * priority. Note: (this.compareTo(LogEvent o) == 0) does not imply that this and o are equal,
	 * nor does it imply that (this.equals(o) == true). It only means that
	 * (this.level.equals(o.level). SEVERE > WARNING > EVENT > OTHER
	 */

	@Override
	public int compareTo(LogEvent o) {

		int result = this.level.value - o.level.value;

		if (result > 0) {
			return -1;
		}

		else if (result < 0) {
			return 1;
		}

		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.className == null) ? 0 : this.className.hashCode());
		result = prime * result + ((this.level == null) ? 0 : this.level.hashCode());
		result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
		result = prime * result + ((this.timeStamp == null) ? 0 : this.timeStamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		LogEvent other = (LogEvent) obj;
		if (this.className == null) {
			if (other.className != null) {
				return false;
			}
		}

		else if (!this.className.equals(other.className)) {
			return false;
		}
		if (this.level != other.level) {
			return false;
		}

		if (this.message == null) {
			if (other.message != null) {
				return false;
			}
		}

		else if (!this.message.equals(other.message)) {
			return false;
		}

		if (this.timeStamp == null) {
			if (other.timeStamp != null) {
				return false;
			}
		}

		else if (!this.timeStamp.equals(other.timeStamp)) {
			return false;
		}

		return true;
	}
}
