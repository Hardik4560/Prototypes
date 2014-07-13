
package com.hardy.logging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class Logger {
    private static String TAG;
    private static String MSG_DIVIDER = "_____";

    private static String dataStorageLocation;
    private static boolean saveToFileSystem;
    private static String logFileName = "logs";

    /**
     * Initializes the default values for writing the logs.
     * @param context - The Application context to which this Logging is to be done.
     * @param savingEnabled - Whether the logs should be saved in a file.
     * @author Hardik
     */
    public static void initialize(Context context) {
        //dataStorageLocation = context.getFilesDir().getAbsolutePath();
        dataStorageLocation = Environment.getExternalStorageDirectory().getAbsolutePath();

        TAG = context.getPackageName();
    }

    public synchronized static void setSaveToFileSystem(boolean saveToFileSystem) {
        Logger.saveToFileSystem = saveToFileSystem;
    }

    public static void d(String TAG, String message) {
        print(Log.DEBUG, TAG, message);
        if (saveToFileSystem) {
            saveLogToFile(new Date().toLocaleString() + MSG_DIVIDER + TAG + MSG_DIVIDER + message);
        }
    }

    public static void e(String TAG, String message) {
        Log.e(TAG, message);
        if (saveToFileSystem) {
            saveLogToFile(new Date().toLocaleString() + MSG_DIVIDER + TAG + MSG_DIVIDER + message);
        }
    }

    public static void e(String TAG, Exception exception) {
        exception.printStackTrace();

        StackTraceElement[] stackTraceElements = exception.getStackTrace();
        StringBuilder error = new StringBuilder();

        for (int i = 0; i < stackTraceElements.length; i++) {
            error.append(TAG + stackTraceElements[i].toString());
        }
        saveLogToFile(new Date().toLocaleString() + error.toString());
    }

    public static void e(String TAG, Throwable throwable) {
        throwable.printStackTrace();

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        StringBuilder error = new StringBuilder();
        for (int i = 0; i < stackTraceElements.length; i++) {
            error.append(TAG + stackTraceElements[i].toString());
        }
        saveLogToFile(new Date().toLocaleString() + error.toString());
    }

    public static void i(String TAG, String message) {
        print(Log.INFO, TAG, message);
    }

    public static void print(int mode, String TAG, String str) {
        if (str.length() > 4000) {
            switch (mode) {
                case Log.DEBUG:
                    Log.d(TAG, str.substring(0, 4000));
                    print(mode, TAG, str.substring(4000));
                    break;
                case Log.INFO:
                    Log.i(TAG, str.substring(0, 4000));
                    print(mode, TAG, str.substring(4000));
                    break;
            }
        }
        else {
            switch (mode) {
                case Log.DEBUG:
                    Log.d(TAG, str);
                    break;
                case Log.INFO:
                    Log.i(TAG, str);
                    break;
            }
        }
    }

    //TODO: Write a method for Getting the logs.
    public static File getLogFile() {
        if (dataStorageLocation != null) {
            return new File(dataStorageLocation + File.separator + logFileName);
        }
        return null;
    }

    public static void saveLogToFile(String message) {
        File f = new File(dataStorageLocation);

        //Creating new folder if not exist.
        if (!f.exists()) {
            f.mkdirs();
        }

        String filename = dataStorageLocation + File.separator + logFileName;
        DataOutputStream dos;
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);
            dos = new DataOutputStream(fos);
            dos.write((message + "\n").getBytes());
            dos.close();
            fos.close();
        }
        catch (FileNotFoundException fe) {
            try {
                //At first this exception will occur and new file will be created.
                File newFile = new File(filename);

                FileOutputStream fos = new FileOutputStream(newFile);
                dos = new DataOutputStream(fos);
                dos.writeUTF(message);
                dos.close();
                fos.close();
            }
            catch (Exception e) {
                Log.e(TAG, "Ironically, an error trying to write a logged error " + e);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Hum, an error trying to write a logged error " + e);
        }
    }

    /**
     * Deletes the log file.
     * This methods is used to perform clean up work every 7th day
     */
    public static boolean deleteLogFile() {
        String filename = dataStorageLocation + File.separator + logFileName;
        File file = new File(filename);
        if (file.exists()) {
            if (!file.delete()) {
                clearLogFile("");
            }
        }
        return false;
    }

    public static void clearLogFile(String message) {
        File f = new File(dataStorageLocation);

        //Creating new folder if not exist.
        if (!f.exists()) {
            f.mkdir();
        }

        String filename = dataStorageLocation + File.separator + logFileName;
        DataOutputStream dos;
        try {
            FileOutputStream fos = new FileOutputStream(filename, false);
            dos = new DataOutputStream(fos);
            dos.write((message + "\n").getBytes());
            dos.close();
            fos.close();
        }
        catch (FileNotFoundException fe) {
            try {
                //At first this exception will occur and new file will be created.
                File newFile = new File(filename);

                FileOutputStream fos = new FileOutputStream(newFile);
                dos = new DataOutputStream(fos);
                dos.writeUTF(message);
                dos.close();
                fos.close();
            }
            catch (Exception e) {
                Log.e(TAG, "Ironically, an error trying to write a logged error " + e);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Hum, an error trying to write a logged error " + e);
        }
    }

}
