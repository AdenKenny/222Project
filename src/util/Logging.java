package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public final class Logging {

	public enum Levels {
		WARNING,
		SEVERE,
		EVENT;
	}

	private static final File LOG_FILE = new File("logs/logs.log");

	public static void logEvent(String className, Levels level, String message) {
		try (FileWriter fileWriter = new FileWriter(LOG_FILE, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

				String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

				String logMessage = timeStamp + " - " + level.toString() + " in " + className + ": " + message;

				printWriter.println(logMessage);
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
