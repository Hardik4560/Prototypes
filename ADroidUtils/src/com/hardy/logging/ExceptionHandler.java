
package com.hardy.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

/**
 * The class listens for any uncaught exceptions. It reports the log to supplied email.
 * @author Hardik
 */
/**
 * @author Hardik
 *
 */
public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

    private Activity context;
    private final String LINE_SEPARATOR = "\n";

    private Class<? extends Activity> errorActivity;

    public static final String EXTRA_ERROR = "error";
    private static final String TAG = ExceptionHandler.class.getSimpleName();

    private String gUsername,
            gPassword,
            gSenderEmail;

    //====================================================
    // CONSTRUCTOR
    //====================================================
    public ExceptionHandler(Activity context, Class<? extends Activity> errorDetailActivity) {
        this.context = context;
        errorActivity = errorDetailActivity;
    }

    protected ExceptionHandler() {}

    //====================================================
    // OVERIDE THE BEHAVIOUR
    //====================================================
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK_INT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        LogIt.e(TAG, errorReport.toString());
        
        if (errorActivity != null) {
            Intent intent = new Intent(context, errorActivity);
            intent.putExtra(EXTRA_ERROR, errorReport.toString());
            context.startActivity(intent);
        }

        //android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    //====================================================
    // GETTERS/SETTERS
    //====================================================
    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public Class<? extends Activity> getErrorActivity() {
        return errorActivity;
    }

    public void setErrorActivity(Class<? extends Activity> errorActivity) {
        this.errorActivity = errorActivity;
    }

    public String getgPassword() {
        return gPassword;
    }

    public void setgPassword(String gPassword) {
        this.gPassword = gPassword;
    }

    public String getgSenderEmail() {
        return gSenderEmail;
    }

    public void setgSenderEmail(String gSenderEmail) {
        this.gSenderEmail = gSenderEmail;
    }

    public String getgUsername() {
        return gUsername;
    }

    public void setgUsername(String gUsername) {
        this.gUsername = gUsername;
    }
}
