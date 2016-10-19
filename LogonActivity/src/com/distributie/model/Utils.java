package com.distributie.model;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Utils {

	public static int getMonthNumber(String monthName) {
		int monthNumber = 0;

		if (monthName.equals("JAN")) {
			monthNumber = 1;
		}

		if (monthName.equals("FEB")) {
			monthNumber = 2;
		}

		if (monthName.equals("MAR")) {
			monthNumber = 3;
		}

		if (monthName.equals("APR")) {
			monthNumber = 4;
		}

		if (monthName.equals("MAY")) {
			monthNumber = 5;
		}

		if (monthName.equals("JUN")) {
			monthNumber = 6;
		}

		if (monthName.equals("JUL")) {
			monthNumber = 7;
		}

		if (monthName.equals("AUG")) {
			monthNumber = 8;
		}

		if (monthName.equals("SEP")) {
			monthNumber = 9;
		}

		if (monthName.equals("OCT")) {
			monthNumber = 10;
		}

		if (monthName.equals("NOV")) {
			monthNumber = 11;
		}

		if (monthName.equals("DEC")) {
			monthNumber = 12;
		}

		return monthNumber;

	}

	public static String getDuration2(String dataStart, String dataStop) {
		String strDuration = "";

		String[] tokenStart = dataStart.split(":");
		String[] tokenStop = dataStop.split(":");

		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, Integer.valueOf(tokenStart[0].substring(0, 4)));
		cal1.set(Calendar.MONTH,
				Utils.getMonthNumber(tokenStart[0].substring(4, 6)));
		cal1.set(Calendar.DAY_OF_MONTH,
				Integer.valueOf(tokenStart[0].substring(6, 8)));
		cal1.set(Calendar.HOUR, Integer.valueOf(tokenStart[1].substring(0, 2)));
		cal1.set(Calendar.MINUTE,
				Integer.valueOf(tokenStart[1].substring(2, 4)));

		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, Integer.valueOf(tokenStop[0].substring(0, 4)));
		cal2.set(Calendar.MONTH,
				Utils.getMonthNumber(tokenStop[0].substring(4, 6)));
		cal2.set(Calendar.DAY_OF_MONTH,
				Integer.valueOf(tokenStop[0].substring(6, 8)));
		cal2.set(Calendar.HOUR, Integer.valueOf(tokenStop[1].substring(0, 2)));
		cal2.set(Calendar.MINUTE, Integer.valueOf(tokenStop[1].substring(2, 4)));

		long milisecs = cal2.getTimeInMillis() - cal1.getTimeInMillis();

		strDuration = getDuration(milisecs);

		return strDuration;
	}

	public static String getDuration(long millis) {

		StringBuilder sb = new StringBuilder(64);

		if (millis > 0) {

			long days = TimeUnit.MILLISECONDS.toDays(millis);
			millis -= TimeUnit.DAYS.toMillis(days);
			long hours = TimeUnit.MILLISECONDS.toHours(millis);
			millis -= TimeUnit.HOURS.toMillis(hours);
			long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
			millis -= TimeUnit.MINUTES.toMillis(minutes);

			if (days > 0) {
				sb.append(days);
				sb.append(" Zile ");
			}
			if (hours > 0) {
				sb.append(hours);
				sb.append(" Ore ");
			}
			if (minutes > 0) {
				sb.append(minutes);
				sb.append(" Minute ");
			}
		}
		return (sb.toString());
	}

	public static String getCodFiliala(String numeFiliala) {
		String fl = "NN10";

		if (numeFiliala.equals("BACAU"))
			fl = "BC10";

		if (numeFiliala.equals("GALATI"))
			fl = "GL10";

		if (numeFiliala.equals("PITESTI"))
			fl = "AG10";

		if (numeFiliala.equals("TIMISOARA"))
			fl = "TM10";

		if (numeFiliala.equals("ORADEA"))
			fl = "BH10";

		if (numeFiliala.equals("FOCSANI"))
			fl = "VN10";

		if (numeFiliala.equals("GLINA"))
			fl = "BU10";

		if (numeFiliala.equals("ANDRONACHE"))
			fl = "BU13";

		if (numeFiliala.equals("OTOPENI"))
			fl = "BU12";

		if (numeFiliala.equals("CLUJ"))
			fl = "CJ10";

		if (numeFiliala.equals("BAIA"))
			fl = "MM10";

		if (numeFiliala.equals("MILITARI"))
			fl = "BU11";

		if (numeFiliala.equals("CONSTANTA"))
			fl = "CT10";

		if (numeFiliala.equals("BRASOV"))
			fl = "BV10";

		if (numeFiliala.equals("PLOIESTI"))
			fl = "PH10";

		if (numeFiliala.equals("PIATRA"))
			fl = "NT10";

		if (numeFiliala.equals("MURES"))
			fl = "MS10";

		if (numeFiliala.equals("IASI"))
			fl = "IS10";

		if (numeFiliala.equals("CRAIOVA"))
			fl = "DJ10";

		return fl;

	}

	
	
	public static String getNumeFiliala(String codFiliala) {
		String fl = "NN10";

		if (codFiliala.equals("BC10"))
			fl = "BACAU";

		if (codFiliala.equals("GL10"))
			fl = "GALATI";

		if (codFiliala.equals("AG10"))
			fl = "PITESTI";

		if (codFiliala.equals("TM10"))
			fl = "TIMISOARA";

		if (codFiliala.equals("BH10"))
			fl = "ORADEA";

		if (codFiliala.equals("VN10"))
			fl = "FOCSANI";

		if (codFiliala.equals("BU10"))
			fl = "GLINA";

		if (codFiliala.equals("BU13"))
			fl = "ANDRONACHE";

		if (codFiliala.equals("BU12"))
			fl = "OTOPENI";

		if (codFiliala.equals("CJ10"))
			fl = "CLUJ";

		if (codFiliala.equals("MM10"))
			fl = "BAIA MARE";

		if (codFiliala.equals("BU11"))
			fl = "MILITARI";

		if (codFiliala.equals("CT10"))
			fl = "CONSTANTA";

		if (codFiliala.equals("BV10"))
			fl = "BRASOV";

		if (codFiliala.equals("PH10"))
			fl = "PLOIESTI";

		if (codFiliala.equals("NT10"))
			fl = "PIATRA NEAMT";

		if (codFiliala.equals("MS10"))
			fl = "MURES";

		if (codFiliala.equals("IS10"))
			fl = "IASI";

		if (codFiliala.equals("DJ10"))
			fl = "CRAIOVA";

		return fl;

	}
	
	
	
	
}
