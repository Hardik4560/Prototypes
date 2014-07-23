
package com.hardy.log;

import java.lang.Thread.UncaughtExceptionHandler;

import android.util.Log;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread arg0, Throwable exception) {
        Log.e("ExceptionHandler", "______Global exception______");
        Logger.e("ExceptionHandler", exception);
        Log.e("ExceptionHandler", "______Global exception handled______");
        System.exit(1);
    }
}
