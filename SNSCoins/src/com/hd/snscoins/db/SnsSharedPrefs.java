
package com.hd.snscoins.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hd.snscoins.logger.Logger;

public class SnsSharedPrefs {

    /**
     * Interface used to allow the class to observe for any changes in {@link SnsSharedPrefs}.
     * @author Roshni
     *
     */
    public interface SharedPrefsListener {

        /**
         * A callback method that will be called when the value of {@link SnsSharedPrefs#DATA_SYNCED} is changed.
        * @param dataSynced
        */
        void onDataSyncedChanged(boolean dataSynced);

        /**
         * A callback method that will be called when the value of {@link SnsSharedPrefs#DATA_SYNCED_STATUS} is changed.
        * @param dataSyncedStatus
        */
        void onDataSyncedStatusChanged(SYNC_STATUS dataSyncedStatus);
    }

    /**
     * Represents the various status while the data is being synced with the server.
     */
    public enum SYNC_STATUS {

        NOT_STARTED,
        IN_PROGRESS,
        SUCCESS,
        FAILED;
    }

    private static final String SFA_SHARED_PREFS = SnsSharedPrefs.class.getSimpleName();

    private static final String TAG = SnsSharedPrefs.class.getSimpleName();

    private List<SharedPrefsListener> mSharedPrefsListeners;

    private SharedPreferences sharedPrefs;
    private Editor editor;
    private Context mAppContext;

    private String DATABASE_INITIALIZED = "databaseInitialized";
    private String USER_LOGGED_IN = "userLoggedIn";
    private String HEADER_TENANT_ID = "headerTenantId";
    private String UNIQUE_ID = "deviceId";
    private String SYNC_URL = "syncUrl";
    private String GCM_TOKEN = "gcmToken";
    private String ROOT_URL = "rootURL";
    private String EMPLOYEE_CODE = "employeeCode";

    /**
     * Represents whether the data is synced or not after login.
     */
    private String DATA_SYNCED = "dataSynced";
    /**
     * Represents  the data sync status. It can be either of {@link SYNC_STATUS}.
     */
    private String DATA_SYNCED_STATUS = "dataSyncStatus";

    private static SnsSharedPrefs mSharedPrefs;

    private SnsSharedPrefs(Context mAppContext) {
        this.mAppContext = mAppContext;
        this.sharedPrefs = mAppContext.getSharedPreferences(SFA_SHARED_PREFS, Activity.MODE_PRIVATE);

        this.mSharedPrefsListeners = new ArrayList<SnsSharedPrefs.SharedPrefsListener>();
        this.editor = sharedPrefs.edit();
    }

    public static void initialize(Context context) {
        if (mSharedPrefs != null) {
            throw new RuntimeException("The SharedPrefernce is already initialized");
        }

        mSharedPrefs = new SnsSharedPrefs(context);
    }

    public static SnsSharedPrefs getInstance() {
        if (mSharedPrefs == null) {
            throw new RuntimeException("Have You missed initializing the object ?");
        }
        return mSharedPrefs;
    }

    public boolean isDatabaseInitialized() {
        return sharedPrefs.getBoolean(DATABASE_INITIALIZED, false);
    }

    public void setDatabaseInitialized(boolean databaseInitialized) {
        editor.putBoolean(DATABASE_INITIALIZED, databaseInitialized).commit();
    }

    public boolean isUserLoggedIn() {
        return sharedPrefs.getBoolean(USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        editor.putBoolean(USER_LOGGED_IN, userLoggedIn).commit();
    }

    public String getHeaderTenantId() {
        return sharedPrefs.getString(HEADER_TENANT_ID, null);
    }

    public void setHeaderTenantId(String headerTenantId) {
        editor.putString(HEADER_TENANT_ID, headerTenantId).commit();
    }

    public String getUniqueId() {
        return sharedPrefs.getString(UNIQUE_ID, null);
    }

    public void setUniqueId(String uniqueId) {
        editor.putString(UNIQUE_ID, uniqueId).commit();
    }

    public boolean isDataSynced() {
        return sharedPrefs.getBoolean(DATA_SYNCED, false);
    }

    public String getSyncUrl() {
        return sharedPrefs.getString(SYNC_URL, null);
    }

    public void setSyncUrl(String syncUrl) {
        editor.putString(SYNC_URL, syncUrl).commit();
    }

    /**
     * Commits the value of {@link SnsSharedPrefs#DATA_SYNCED} in the underlying {@link SharedPreferences} 
     * and also notifies the observers about the change.
     * @param dataSynced
     */
    public void setDataSynced(boolean dataSynced) {
        boolean success = editor.putBoolean(DATA_SYNCED, dataSynced).commit();
        if (success) {
            for (Iterator<SharedPrefsListener> iterator = mSharedPrefsListeners.iterator(); iterator.hasNext();) {
                iterator.next().onDataSyncedChanged(dataSynced);
            }
        }
    }

    public synchronized SYNC_STATUS getDataSyncedStatus() {
        String status = sharedPrefs.getString(DATA_SYNCED_STATUS, SYNC_STATUS.NOT_STARTED.toString());
        return SYNC_STATUS.valueOf(status);
    }

    /**
     * Commits the value of {@link SnsSharedPrefs#DATA_SYNCED_STATUS} in the underlying {@link SharedPreferences} 
     * and also notifies the observers about the change.
     * @param dataSyncedStatus
     */
    public synchronized void setDataSyncedStatus(SYNC_STATUS dataSyncedStatus) {
        boolean success = editor.putString(DATA_SYNCED_STATUS, dataSyncedStatus.toString()).commit();
        if (success) {
            synchronized (mSharedPrefsListeners) {
                for (Iterator<SharedPrefsListener> iterator = mSharedPrefsListeners.iterator(); iterator.hasNext();) {
                    iterator.next().onDataSyncedStatusChanged(dataSyncedStatus);
                }
            }
        }
    }

    public synchronized void attach(SharedPrefsListener ptSharedPrefsListener) {
        if (ptSharedPrefsListener != null) {
            synchronized (mSharedPrefsListeners) {
                mSharedPrefsListeners.add(ptSharedPrefsListener);
            }
        }
    }

    public void detach(SharedPrefsListener ptSharedPrefsListener) {
        if (ptSharedPrefsListener != null) {
            synchronized (mSharedPrefsListeners) {
                mSharedPrefsListeners.remove(ptSharedPrefsListener);
            }
        }
    }

    public String getGCMToken() {
        return sharedPrefs.getString(GCM_TOKEN, null);
    }

    public void setGcmToken(String gcmToken) {
        editor.putString(GCM_TOKEN, gcmToken).commit();
    }

    public String getRootURL() {
        return sharedPrefs.getString(ROOT_URL, null);
    }

    public void setRootURL(String rOOT_URL) {
        editor.putString(ROOT_URL, rOOT_URL).commit();
    }

    public String getEmployeeCode() {
        return sharedPrefs.getString(EMPLOYEE_CODE, null);
    }

    public void setEmployeeCode(String employee) {
        editor.putString(EMPLOYEE_CODE, employee).commit();
    }

    public void resetSharedPrefsValue() {
        Logger.d(TAG, "Resetting shared preference");
        editor.clear();

        editor.putBoolean(DATABASE_INITIALIZED, false).commit();
        editor.putBoolean(USER_LOGGED_IN, false).commit();
        editor.putString(HEADER_TENANT_ID, null).commit();
        editor.putString(UNIQUE_ID, null).commit();
        editor.putString(SYNC_URL, null).commit();
        editor.putBoolean(DATA_SYNCED, false).commit();
        editor.putString(DATA_SYNCED_STATUS, null).commit();
        editor.putString(EMPLOYEE_CODE, null).commit();

    }
}
