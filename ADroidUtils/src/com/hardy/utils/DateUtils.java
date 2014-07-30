
package com.hardy.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateUtils {
    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static int getDaysOfMonth(int month, int year) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, 1);
        int actualMaxDays = gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return actualMaxDays;

    }

    public static String getMonthName(int month) {
        String monthNames[] = new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

        return monthNames[month];
    }
    
    public static String getShortMonthName(int month) {
        String monthNames[] = new String[] { "Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec" };

        return monthNames[month];
    }
}
