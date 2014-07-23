
package com.hd.snscoins.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hardy.exceptions.ObjectNotInitializedException;
import com.hd.snscoins.core.DaoMaster;
import com.hd.snscoins.core.DaoMaster.DevOpenHelper;
import com.hd.snscoins.core.DaoSession;

public class SnsDatabase {
    private static final String DATABASE_NAME = "sns.sqlite";
    private static SnsDatabase database;

    private static DaoSession daoSession;
    private static SQLiteDatabase db;

    public static final String TABLE_COIN = "product";
    public static final String TABLE_COIN_TYPE = "type";
    public static final String TABLE_COIN_SUB_TYPE = "sub_type";
	public static final String TABLE_YEAR = "year";
	
	public static final String TABLE_EVENTS = "events";
	public static final String TABLE_NEWS = "news";

    private SnsDatabase(Context context) {
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DATABASE_NAME, null);

        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        SnsDatabase.db = db;
    }

    public synchronized static SnsDatabase initialize(Context context) {
        if (database != null) {
            throw new RuntimeException("Multiple calls to initialize(...)");
        }

        database = new SnsDatabase(context);
        return database;
    }

    public synchronized static DaoSession session() {
        if (database == null || daoSession == null) {
            throw new ObjectNotInitializedException(SnsDatabase.class);
        }
        return daoSession;
    }

    public synchronized static SQLiteDatabase db() {
        if (database == null || db == null) {
            throw new ObjectNotInitializedException(SnsDatabase.class);
        }
        return db;
    }
}
