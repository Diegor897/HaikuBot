package haikubot;

import java.time.Instant;
import java.time.LocalDateTime;

public final class TimerService {
	
	private TimerService() {
		// EMPTY
	}
	
	public static LocalDateTime getTime(double minutesOffSet) {
		String s = Instant.now().minusSeconds((long)(minutesOffSet * 60.0)).toString();
		
		return LocalDateTime.parse(s.substring(0, s.length() - 1));
	}
	
	public static boolean minsPassed(double minutes, long startTime) {
		return (System.currentTimeMillis() - startTime) > (long)(60000.0 * minutes);
	}
}