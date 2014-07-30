
package com.hardy.logging;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.hardy.utils.emailutils.DroidMailer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Class that handles the logging mechanism of an application. You should {@link LogIt#initialize(Context, Class)} in your
 * application class before using any of the methods of this class.
 * Saving to the file system is by default not enabled.
 * TODO: Add log file rotation engineering
 * @author Hardik
 */
public class LogIt {

    //==============================================
    // VARIABLES
    //==============================================
    private static String TAG;
    private static String MSG_DIVIDER = "_____";

    private static String dataStorageLocation;

    protected final static String ACTION_SEND_LOG = "com.hardy.utils.action_send_logs";

    /** If enabled, will write the file to the system, so that it can be used later */
    private static boolean saveToFileSystem;
    private static String logFileName = "logs";

    private static ExceptionHandler mExceptionHandler;
    private static Context mContext;
    private static String username;
    private static String password;
    private static String mailTO;
    private static String subject;
    private static String body;

    //==============================================
    // INITIALIZE
    //==============================================
    /**
     * Initializes the default values for writing the logs.
     * @param context - The Application context to which this Logging is to be done.
     * @param errorDetailActivity - The activity to launch when an error is occured.
     * @author Hardik
     */
    public static void initialize(Context context) {
        mContext = context;

        dataStorageLocation = context.getFilesDir().getAbsolutePath();
        //dataStorageLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
        TAG = context.getPackageName();
    }

    /**
     * Sets the default exception handler.  
     * @param context - The activity context that wants to handle the unexpected exceptions.
     * @param errorDetailActivity - The activity that should be launched when and error occurs. 
     * Pass null if the application should just get closed.
     * @author Hardik
     * @see ExceptionHandler 
     */
    public static synchronized void setDefaultExceptionHandler(Activity context, Class<? extends Activity> errorDetailActivity) {
        if (context == null) {
            throw new NullPointerException("Context cannot be null when setting exception handler");
        }

        if (mExceptionHandler == null) {
            mExceptionHandler = new ExceptionHandler();
        }

        mExceptionHandler.setContext(context);
        mExceptionHandler.setErrorActivity(errorDetailActivity);

        setExceptionHandler(mExceptionHandler);
    }

    /**
     * Sets the supplied exception handler.   
     * @param context - The activity context that wants to handle the unexpected exceptions.
     * @param errorDetailActivity - The activity that should be launched when and error occurs. 
     * Pass null if the application should just get closed.
     * @author Hardik
     * @see ExceptionHandler 
     */
    public static synchronized void setExceptionHandler(ExceptionHandler exceptionHandler) {
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler);
    }

    public static void setRepeatingLogSendingEnabled(long intervalTimeInMillis) {
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        Intent sendLogsIntent = new Intent(mContext, SendLogsReceiver.class);
        sendLogsIntent.setAction(ACTION_SEND_LOG);

        Bundle extras = new Bundle();
        extras.putString(DroidMailer.EXTRA_USERNAME, username);
        extras.putString(DroidMailer.EXTRA_PASSWORD, password);
        extras.putString(DroidMailer.EXTRA_MAIL_TO, mailTO);
        extras.putString(DroidMailer.EXTRA_SUBJECT, subject);
        extras.putString(DroidMailer.EXTRA_BODY, body);
        sendLogsIntent.putExtras(extras);

        PendingIntent operation = PendingIntent.getBroadcast(mContext, 404, sendLogsIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + intervalTimeInMillis, intervalTimeInMillis, operation);
        LogIt.i(TAG, "Alarm set = " + System.currentTimeMillis() + intervalTimeInMillis);
    }

    //==============================================
    // CLASS METHODS
    //==============================================

    /**
     * Send a DEBUG log message.
     * <br>
     * Method can be used to log the information. The logs will be written to a file if saving logs to file system is enabled.
     * @param TAG Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @author Hardik
     * @see {@link LogIt#setSaveToFileSystem(boolean)}
     */
    public static void d(String TAG, String message) {
        print(Log.DEBUG, TAG, message);
        if (saveToFileSystem) {
            save(new Date().toLocaleString() + MSG_DIVIDER + TAG + MSG_DIVIDER + message);
        }
    }

    /**
     * Send an ERROR log message.
     * Method can be used to log the information. The logs will be written to a file if saving logs to file system is enabled.
     * @param TAG Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @author Hardik
     * @see {@link LogIt#setSaveToFileSystem(boolean)}
     */
    public static void e(String TAG, String message) {
        Log.e(TAG, message);
        if (saveToFileSystem) {
            save(new Date().toLocaleString() + MSG_DIVIDER + TAG + MSG_DIVIDER + message);
        }
    }

    /**
     * Send an ERROR log message.
     * Method can be used to log the information. The logs will be written to a file if saving logs to file system is enabled.
     * @param TAG Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param exception message The exception you would like logged. The stacktrack of the exception would be printed.
     * @author Hardik
     * @see {@link LogIt#setSaveToFileSystem(boolean)}
     */
    public static void e(String TAG, Exception exception) {
        exception.printStackTrace();

        if (saveToFileSystem) {
            StackTraceElement[] stackTraceElements = exception.getStackTrace();
            StringBuilder error = new StringBuilder();

            for (int i = 0; i < stackTraceElements.length; i++) {
                error.append(TAG + stackTraceElements[i].toString());
            }
            save(new Date().toLocaleString() + error.toString());
        }
    }

    /**
     * Send an ERROR log message.
     * Method can be used to log the information. The logs will be written to a file if saving logs to file system is enabled.
     * @param TAG Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param throwable message The {@link Throwable} you would like logged. The stacktrack of the exception would be printed.
     * @author Hardik
     * @see {@link LogIt#setSaveToFileSystem(boolean)}
     */
    public static void e(String TAG, Throwable throwable) {
        throwable.printStackTrace();

        StackTraceElement[] stackTraceElements = throwable.getStackTrace();
        StringBuilder error = new StringBuilder();
        for (int i = 0; i < stackTraceElements.length; i++) {
            error.append(TAG + stackTraceElements[i].toString());
        }
        save(new Date().toLocaleString() + error.toString());
    }

    /**
     * Method can be used to log the information. Logs written by this method won't be saved to the file even when {@link saveToFileSystem}
     * @param TAG Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like logged.
     * @author Hardik
     */
    public static void i(String TAG, String message) {
        print(Log.INFO, TAG, message);
    }

    private static void print(int mode, String TAG, String str) {
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

    private static void save(String message) {
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
     * This methods is used to perform clean up works, log file rotation.
     * @author Hardik
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

    private static void clearLogFile(String message) {
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

    //==============================================
    // GETTER/SETTER
    //==============================================
    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String tAG) {
        TAG = tAG;
    }

    /**
     * Enables the logs to be saved to the file system.
     * @param saveToFileSystem - true incase the logs have to be saved.
     * @author Hardik
     */
    public synchronized static void setSaveToFileSystem(boolean saveToFileSystem) {
        LogIt.saveToFileSystem = saveToFileSystem;
    }

    /**
     * Gets log file if log saving was enabled. 
     * @return {@link File} that contains the logs if saving logs was enabled else null
     */
    public static File getLogFile() {
        if (dataStorageLocation != null && saveToFileSystem) {
            return new File(dataStorageLocation + File.separator + logFileName);
        }
        return null;
    }

    public static void setUsername(String username) {
        LogIt.username = username;
    }

    public static void setPassword(String password) {
        LogIt.password = password;
    }

    public static void setMailTO(String mailTO) {
        LogIt.mailTO = mailTO;
    }

    public static void setSubject(String subject) {
        LogIt.subject = subject;
    }

    public static void setBody(String body) {
        LogIt.body = body;
    }
}
