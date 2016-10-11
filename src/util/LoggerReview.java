package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public final class LoggerReview {

	List<LogEvent> listOfEvents;

	public LoggerReview() {

	}

	private void getEvents() {

		File logFile = new File("logs/logs.log");

		try {
			Scanner scanner = new Scanner(logFile);

			this.listOfEvents = new ArrayList<>();

			while(scanner.hasNextLine()) {
				String fullLine = scanner.nextLine();

				String[] split1 = fullLine.split("-");
				String[] split2 = split1[1].trim().split(":");
				String[] split3 = split2[0].split("in");

				String time = split1[0].trim();
				String message = split2[1].trim();
				String eventLevel = split3[0].trim();
				String className = split3[1].trim();

				LogEvent event = new LogEvent(className, eventLevel, message, time);

				this.listOfEvents.add(new LogEvent(className, eventLevel, message, time));
			}
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void orderEvents() {
		Collections.sort(this.listOfEvents);

		for(LogEvent e : this.listOfEvents) {
			System.out.println(e.out());
		}
	}

	public static void main(String[] args) {
		LoggerReview rev = new LoggerReview();

		rev.getEvents();
		rev.orderEvents();
	}

}
