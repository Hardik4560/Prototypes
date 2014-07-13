
package com.hd.snscoins.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hd.snscoins.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import com.neebal.android.core.model.Model;
import com.neebal.android.core.model.db.CustomSqliteOpenHelper;

public class SnsDatabase extends CustomSqliteOpenHelper {

    public static final String DATABASE_NAME = "UpDatabase.sqlite";
    public static final int DB_VERSION = 1;

    private static final String TAG = SnsDatabase.class.getSimpleName();

    private static List<Class> classTableList;

    public SnsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);

        classTableList = new ArrayList<Class>();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            for (Iterator<Class> iterator = classTableList.iterator(); iterator.hasNext();) {
                Class classToCreate = iterator.next();
                Model.create(classToCreate);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
            int arg3) {}

    /**
     * 
     * @author Hardik Shah
     */
    public static void resetDatabase() {
        Logger.d(TAG, "Clearing the tables");

        try {
            for (Iterator<Class> iterator = classTableList.iterator(); iterator.hasNext();) {
                Class classToDrop = iterator.next();
                Model.drop(classToDrop);
            }

            for (Iterator<Class> iterator = classTableList.iterator(); iterator.hasNext();) {
                Class classToCreate = iterator.next();
                Model.create(classToCreate);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
