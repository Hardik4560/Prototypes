
package com.hd.snscoins.application;

import android.app.Application;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EApplication;
import com.hardy.logging.Logger;
import com.hardy.utils.SharedPrefs;
import com.hardy.utils.ToastMaker;
import com.hd.snscoins.core.CoinSubType;
import com.hd.snscoins.db.SnsDatabase;
import com.hd.snscoins.fixtures.SnsFixtureDataCreator;
import com.hd.snscoins.network.NetworkController;
import com.hd.snscoins.utils.SnsBuildConfiguration;

/**
 * The application class.
 * @author Hardik
 */
@EApplication
public class SnSCoreSystem extends Application {

    private static final String TAG = SnSCoreSystem.class.getSimpleName();

    public static final int STAGE_1 = 1;
    public static final String DATABASE_INITIALIZED = "database_init";

    @Bean
    SnsFixtureDataCreator fixtureDataCreator;

    private CoinSubType transientSubType;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize simply reliable file.
        Logger.initialize(this);
        Logger.setSaveToFileSystem(SnsBuildConfiguration.LOG_SAVING_ENABLED);
        Logger.d(TAG, "Application Started");

        ToastMaker.initialize(this);
        NetworkController.initialize(this);

        //Initalize the database
        SnsDatabase.initialize(this);
        SharedPrefs.initialize(this);

        /*if (!(Boolean) SharedPrefs.getInstance().get(DATABASE_INITIALIZED, false)) {
            fixtureDataCreator.createFixtureData(this);
        }*/
    }

    public void setTransientSubType(CoinSubType transientSubType) {
        this.transientSubType = transientSubType;
    }

    public CoinSubType getTransientSubType() {
        return this.transientSubType;
    }
}
