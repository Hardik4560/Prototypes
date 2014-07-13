
package com.hd.snscoins.core;

import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.logger.Logger;
import com.hd.snscoins.utils.SnsBuildConfiguration;
import com.neebal.android.core.customappcomp.CustomApplication;

/**
 * The application class.
 * @author Hardik
 */
public class SnSCoreSystem extends CustomApplication {

    private static final String TAG = SnSCoreSystem.class.getSimpleName();

    public static final int STAGE_1 = 1;

    public SnSCoreSystem() {
        super(SnsDatabase.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize simply reliable file.
        Logger.initialize(this);
        Logger.setSaveToFileSystem(SnsBuildConfiguration.LOG_SAVING_ENABLED);
        Logger.d(TAG, "Application Started");
    }
}
