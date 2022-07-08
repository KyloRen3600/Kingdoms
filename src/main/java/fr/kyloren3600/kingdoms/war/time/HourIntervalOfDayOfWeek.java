package fr.kyloren3600.kingdoms.war.time;

import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;

public final class HourIntervalOfDayOfWeek implements TimeInterval {

	private final int day;
	private final int startHour;
	private final int startMinute;
	private final int endHour;
	private final int endMinute;

	public HourIntervalOfDayOfWeek(int day, int startHour, int startMinute, int endHour, int endMinute) {
		//1 = sunday, 7 = saturday
		if (!isInRange(day, 1, 7) || !isInRange(startHour, 0, 23) || !isInRange(startMinute, 0, 59) || !isInRange(endHour, 0, 23) || !isInRange(endMinute, 0, 59)) throw new IllegalArgumentException();
		if (endHour < startHour || (startHour == endHour && endMinute < startMinute)) throw new IllegalArgumentException();
		this.day = day;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.endHour = endHour;
		this.endMinute = endMinute;
	}

	@Override
	public boolean isNow() {
		Date now = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(now);

		int currentDay = cal.get(Calendar.DAY_OF_WEEK);
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);
		int currentMinute = cal.get(Calendar.MINUTE);
		return (currentDay) == day && isInRange(currentHour, startHour, endHour) && (currentHour != endHour || isInRange(currentMinute, startMinute, endMinute));
	}

	private boolean isInRange(int value, int a, int b) {
		return (a <= value && value <= b);
	}

	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner(", ", "", "");
		stringJoiner.add(String.valueOf(day));
		stringJoiner.add(String.valueOf(startHour));
		stringJoiner.add(String.valueOf(startMinute));
		stringJoiner.add(String.valueOf(endHour));
		stringJoiner.add(String.valueOf(endMinute));
		return stringJoiner.toString();
	}
}
