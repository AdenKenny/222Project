package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * A class to represent logged events and to review them.
 *
 * @author Aden
 */

public final class LoggerReview {

	private static List<LogEvent> listOfEvents; //A list of all the events.

	/**
	 * This shouldn't ever be initialised.
	 */

	private LoggerReview() {
		throw new AssertionError();
	}


	/**
	 * Reads the logged events from the log file.
	 */

	private static void getEvents() {

		File logFile = new File("logs/logs.log"); //File that the logs are stored in.

		try {
			Scanner scanner = new Scanner(logFile);

			listOfEvents = new ArrayList<>();

			while(scanner.hasNextLine()) {
				String fullLine = scanner.nextLine(); //Get the event.

				String[] split1 = fullLine.split("-"); //Dodgy splits.
				String[] split2 = split1[1].trim().split(":");
				String[] split3 = split2[0].split("in");

				String time = split1[0].trim(); //Assign variables from splits.
				String message = split2[1].trim();
				String eventLevel = split3[0].trim();
				String className = split3[1].trim();

				listOfEvents.add(new LogEvent(className, eventLevel, message, time)); //Create event.
			}
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sorts the events that have been loaded. They will be sorted by event level.
	 */

	public static void orderEvents() {
		Collections.sort(listOfEvents);
	}

	/**
	 * Returns a List<LogEvent> that contains all events that have been loaded in.
	 *
	 * @return A List<LogEvent>
	 */

	public static List<LogEvent> getAllEvents() {
		return listOfEvents;
	}
}
