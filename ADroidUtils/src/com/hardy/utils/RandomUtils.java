
package com.hardy.utils;

import java.util.Calendar;
import java.util.Random;

public class RandomUtils {
    /**
     * Creates a random string with the specified length. If the length is provided 
     * as 0, then a random string of random length is created and returned.
     * @param prefix - The prefix for the string if any.
     * @param length - The number of the characters required in the string. 
     * @return - A random generated string.
     * @author Hardik Shah 
     */
    public static String getRandomString(String prefix, int length) {
        Random rndGenerator = new Random();
        int nameLength;
        if (length == 0) {
            nameLength = rndGenerator.nextInt(10) + 4;
        }
        else {
            nameLength = length;
        }

        String name = "";
        for (int j = 0; j < nameLength; j++) {
            int ascii = rndGenerator.nextInt(26) + 65;
            name += (char) ascii;
        }
        return prefix + name.toLowerCase();
    }

    /**
     * Creates a random digits with the specified length . If the length is provided 
     * as 0, then a random string of random length is created and returned.
     * @param prefix - The prefix for the string if any.
     * @param length - The number of the characters required in the string. 
     * @return - A random generated string.
     * @author Hardik Shah 
     */
    public static String getRandomDigits(String prefix, int length) {
        Random rndGenerator = new Random();
        int nameLength;
        if (length == 0) {
            nameLength = rndGenerator.nextInt(10) + 4;
        }
        else {
            nameLength = length;
        }

        String name = "";
        for (int j = 0; j < nameLength; j++) {
            int number = rndGenerator.nextInt(10);
            name += number;
        }
        return prefix + name;
    }

    /**
     * Give a random true or false.
     * @return
     */
    public static boolean getSwitchStatus() {
        Random rndGenerator = new Random();

        int switchValue = rndGenerator.nextInt(2);
        return switchValue == 1 ? true : false;
    }

    /**
     * This gives you a random number in between from (inclusive) and to (exclusive)
     * @param low - The lower valued number
     * @param high - The higher valued number.
     * @return  - A random number between low and high
     * @author Hardik
     */
    public static int getRandomNumberBetween(int low, int high) {
        if (low >= high) {
            return -1;
        }
        Random rndGenerator = new Random();

        int value = rndGenerator.nextInt(high - low) + low;
        return value;
    }

    /**
     * This gives you a random number in between from (inclusive) and to (exclusive)
     * @param low - The lower valued number
     * @param high - The higher valued number.
     * @return  - A random number between low and high
     * @author Hardik
     */
    public static long getRandomNumberBetween(long low, long high) {
        Random rndGenerator = new Random();

        long value = rndGenerator.nextInt((int) (high - low)) + low;
        return value;
    }

    /**
     * The method returns a randomDate object, the method takes of returning the day based on the month
     * and max days for the month.
     * @param month - The month for which the date should be created.
     * @return - A calendar object for the given month. The day of the month will be random.
     * @author Hardik
     */
    public static Calendar getRandomCalendar(int month) {
        int[] daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        Calendar date = Calendar.getInstance();

        int dayOfMonth = daysOfMonth[month];

        int day = getRandomNumberBetween(1, dayOfMonth + 1);

        date.set(Calendar.DATE, day);
        date.set(Calendar.MONTH, month);

        return date;
    }
}
