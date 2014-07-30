
package com.hardy.application;

import com.hardy.logging.LogIt;
import com.hardy.network.NetworkController;
import com.hardy.utils.ToastMaker;

import android.app.Application;

public class Core extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogIt.initialize(this);
        LogIt.setSaveToFileSystem(true);
        NetworkController.initialize(this);
        ToastMaker.initialize(this);
    }
}
