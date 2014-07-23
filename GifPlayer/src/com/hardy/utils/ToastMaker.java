
package com.hardy.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class ToastMaker {

    private Context mContext;
    private static ToastMaker toastMaker;
    private Toast toast;

    private ToastMaker(Context appContext) {
        super();
        this.mContext = appContext;
    }

    public static void initialize(Context context) {
        if (toastMaker != null) {
            throw new RuntimeException("The Object has already being intialized, Call getInstance to get an instance of the class");
        }
        else {
            toastMaker = new ToastMaker(context);
        }
    }

    public static ToastMaker getInstance() {
        if (toastMaker == null) {
            //Object not initialized.
            throw new RuntimeException();
        }
        return toastMaker;
    }

    public void createToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }

                toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void createToast(final String message, final int gravity, final int offSetX, final int offSetY, final int duration) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast.makeText(mContext, message, duration);
                toast.setGravity(gravity, offSetX == -1 ? Gravity.CENTER : offSetX, offSetY == -1 ? Gravity.CENTER : offSetY);
                toast.show();
            }
        });
    }
}
