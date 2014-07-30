
package com.hardy.logging;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hardy.utils.emailutils.DroidMailer;

public class SendLogsReceiver extends BroadcastReceiver {

    private static final String TAG = SendLogsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(LogIt.ACTION_SEND_LOG)) {

            Bundle extras = intent.getExtras();

            if (extras != null) {
                String userName = intent.getStringExtra(DroidMailer.EXTRA_USERNAME);
                String password = intent.getStringExtra(DroidMailer.EXTRA_PASSWORD);
                String mailTo = intent.getStringExtra(DroidMailer.EXTRA_MAIL_TO);
                String subject = intent.getStringExtra(DroidMailer.EXTRA_SUBJECT);
                String body = intent.getStringExtra(DroidMailer.EXTRA_BODY);

                //Send email
                DroidMailer dm = new DroidMailer(context);
                dm.setGmailUserName(userName);
                dm.setGmailPassword(password);
                dm.setMailTo(mailTo);
                dm.setFormSubject(subject);

                File file = LogIt.getLogFile();
                if (file != null) {
                    dm.setFormBody("PFA,\n" + body);
                    dm.setAttachment(LogIt.getLogFile());
                }
                else {
                    dm.setFormBody(body);
                }

                dm.setProcessVisibility(false);
                dm.send();
            }
        }
    }
}
