package util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	
	public static boolean isAgeBetween18And70(String rawDate) {
		if (rawDate == null || rawDate.length() != 13) {
			return false; // loš format
		}
		
		try {
			String day = rawDate.substring(0, 2);
			String month = rawDate.substring(2, 4);
			int yearShort = Integer.parseInt(rawDate.substring(4, 7)); // e.g. 985 or 001
			
			int fullYear;
			if (yearShort >= 900) {
				fullYear = 1000 + yearShort; // 985 → 1985
			} else {
				fullYear = 2000 + yearShort; // 001 → 2001
			}
			
			String fullDate = day + month + fullYear;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
			LocalDate dob = LocalDate.parse(fullDate, formatter);
			
			int age = Period.between(dob, LocalDate.now()).getYears();
			return age >= 18 && age <= 70;
			
		} catch (Exception e) {
			return false; // neispravan datum
		}
	}
}
