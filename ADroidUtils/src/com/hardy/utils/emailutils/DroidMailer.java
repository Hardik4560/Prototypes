
package com.hardy.utils.emailutils;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.hardy.logging.LogIt;
import com.hardy.network.NetworkController;
import com.hardy.utils.ToastMaker;

/**
 * @author Hardik Shah
 */
public class DroidMailer {
    public static final String EXTRA_USERNAME = "username";
    public static final String EXTRA_PASSWORD = "password";
    public static final String EXTRA_MAIL_TO = "mailTo";
    public static final String EXTRA_SUBJECT = "subject";
    public static final String EXTRA_BODY = "body";

    String TAG = DroidMailer.class.getSimpleName();

    //===========================================
    // VARIABLES
    //===========================================
    String username,
            password,
            mailto,
            subject,
            body,
            sendingMessage,
            sendingMessageSuccess;

    File attachment;

    boolean processVisibility = true;
    Context mContext;

    //===========================================
    // CONSTRUCTORS
    //===========================================
    public DroidMailer(Context context) {
        this.mContext = context;
    }

    //===========================================
    // SETTER/GETTERS
    //===========================================
    public void setGmailUserName(String string) {
        this.username = string;
    }

    public void setGmailPassword(String string) {
        this.password = string;
    }

    public void setProcessVisibility(boolean state) {
        this.processVisibility = state;
    }

    public void setMailTo(String string) {
        this.mailto = string;
    }

    public void setFormSubject(String string) {
        this.subject = string;
    }

    public void setFormBody(String string) {
        this.body = string;
    }

    public void setSendingMessage(String string) {
        this.sendingMessage = string;
    }

    public void setSendingMessageSuccess(String string) {
        this.sendingMessageSuccess = string;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    //===========================================
    // CLASS METHODS
    //===========================================
    public void send() {
        boolean valid = true;

        if (username == null && username.isEmpty()) {
            LogIt.e(TAG, "You didn't set Gmail username!");
            valid = false;
        }
        if (password == null && password.isEmpty()) {
            LogIt.e(TAG, "You didn't set Gmail password!");
            valid = false;
        }
        if (mailto == null && mailto.isEmpty()) {
            LogIt.e(TAG, "You didn't set email recipient!");
            valid = false;
        }
        if (!NetworkController.getInstance().isNetworkAvailable()) {
            LogIt.e(TAG, "User don't have internet connection!");
            valid = false;
        }
        if (valid == true) {
            new startSendingEmail().execute();
        }
    }

    public class startSendingEmail extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            if (processVisibility) {
                pd = new ProgressDialog(mContext);

                if (sendingMessage != null && !sendingMessage.isEmpty()) {
                    pd.setMessage(sendingMessage);
                }
                else {
                    LogIt.d(TAG, "We dont have sending message so we use generic");
                    pd.setMessage("Loading...");
                }
                pd.setCancelable(false);
                pd.show();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                GMailSender sender = new GMailSender(username, password);
                sender.sendMail(subject, body, username, mailto, attachment);
            }
            catch (Exception e) {
                LogIt.e(TAG, e.getMessage().toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (processVisibility) {
                pd.dismiss();
                if (sendingMessageSuccess != null
                    && !sendingMessageSuccess.isEmpty()) {
                    ToastMaker.getInstance().createToast(sendingMessageSuccess);
                }
                else {
                    LogIt.d(TAG, "We dont have sending success message so we use generic");
                    ToastMaker.getInstance().createToast("Your message was sent successfully.");
                }
            }
            super.onPostExecute(result);
        }
    }

}
