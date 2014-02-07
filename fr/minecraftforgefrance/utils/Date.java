package fr.minecraftforgefrance.utils;

import java.util.Calendar;

public class Date {
	private static final Calendar CALENDAR = Calendar.getInstance();
	
	public static String getJJMMAAAAHMS(){
		return (CALENDAR.get(Calendar.DAY_OF_MONTH) + "/" + CALENDAR.get(Calendar.MONTH) + "/" + CALENDAR.get(Calendar.YEAR) 
				+ "-" + CALENDAR.get(Calendar.HOUR_OF_DAY) + ":" + CALENDAR.get(Calendar.MINUTE) + ":" + CALENDAR.get(Calendar.SECOND));
	}
}
