
package com.hardy.utils;

import com.hardy.exceptions.ObjectNotInitializedException;
import com.hardy.logging.Logger;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * An utility class that gives you the flexibilty to generate toast from any part of your
 * application. 
 * @author Hardy
 */
public class ToastMaker {

    private static final String TAG = ToastMaker.class.getSimpleName();
    private Context mContext;
    private static ToastMaker toastMaker;
    private Toast toast;

    private ToastMaker(Context appContext) {
        super();
        this.mContext = appContext;
    }

    public static void initialize(Context context) {
        if (toastMaker != null) {
            throw new ObjectNotInitializedException(ToastMaker.class);
        }
        else {
            toastMaker = new ToastMaker(context);
        }
    }

    /**
     * Gives an instance of this class.
     * <b>NOTE: Be sure you have called initialized before calling this method
     * @return A global instance of {@link ToastMaker}
     * @author Hardy
     */
    public static ToastMaker getInstance() {
        if (toastMaker == null) {
            //Object not initialized.
            throw new RuntimeException();
        }
        return toastMaker;
    }

    /**
     * Simply creates a toast message
     * @param message - The message to be shown on the toast.
     */
    public void createToast(final String message) {
        if (message == null) {
            Logger.e(TAG, "Failed to create toast, as the message was passed null");
            return;
        }
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

    /**
     * Make a standard toast that just contains a text view.
     * @param message The text to show. Can be formatted text.
     * @param gravity Set the location at which the notification should appear on the screen.
     * @param offSetX The distance to be skipped in pixels from the left.
     * @param offSetY The distance to be skipped in pixels from the top.
     * @param duration How long to display the message. Either LENGTH_SHORT or LENGTH_LONG
     * @author Hardy 
     */
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
