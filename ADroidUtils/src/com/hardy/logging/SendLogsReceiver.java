
package com.hardy.logging;

import java.io.File;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hardy.utils.emailutils.DroidMailer;
import com.hardy.utils.emailutils.DroidMailer.OnMailSendListener;

public class SendLogsReceiver extends BroadcastReceiver {

    private static final String TAG = SendLogsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(LogIt.ACTION_SEND_LOG)) {

            Bundle extras = intent.getExtras();

            if (extras != null) {
                String userName = extras.getString(DroidMailer.EXTRA_USERNAME);
                String password = extras.getString(DroidMailer.EXTRA_PASSWORD);
                String mailTo = extras.getString(DroidMailer.EXTRA_MAIL_TO);
                String subject = extras.getString(DroidMailer.EXTRA_SUBJECT);
                String body = extras.getString(DroidMailer.EXTRA_BODY);

                //Send email
                DroidMailer dm = new DroidMailer(context);
                dm.setGmailUserName(userName);
                dm.setGmailPassword(password);
                dm.setMailTo(mailTo);
                dm.setFormSubject(subject + " " + new Date().toLocaleString());

                File file = LogIt.getLogFile();
                if (file != null) {
                    dm.setFormBody("PFA,\n" + body);
                    dm.setAttachment(LogIt.getLogFile());
                }
                else {
                    dm.setFormBody(body);
                }

                dm.setProcessVisibility(false);
                dm.setOnMailSendListener(new OnMailSendListener() {

                    @Override
                    public void onMailSuccessfully() {
                        LogIt.deleteLogFile();
                    }

                    @Override
                    public void onMailSendingFailed() {}
                });

                LogIt.i(TAG, "Sending Email now !");
                dm.send();
            }
        }
    }
}
