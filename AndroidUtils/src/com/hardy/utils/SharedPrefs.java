
package com.hardy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hardy.logging.Logger;

public class SharedPrefs {
    private static final String TAG = SharedPrefs.class.getSimpleName();

    private SharedPreferences sharedPrefs;
    private Editor editor;
    private Context mAppContext;

    private static SharedPrefs mSharedPrefs;

    private SharedPrefs(Context mAppContext) {
        this.mAppContext = mAppContext;
        this.sharedPrefs = mAppContext.getSharedPreferences(TAG, Activity.MODE_PRIVATE);

        this.editor = sharedPrefs.edit();
    }

    public static void initialize(Context context) {
        if (mSharedPrefs != null) {
            throw new RuntimeException("The SharedPrefernce is already initialized");
        }

        mSharedPrefs = new SharedPrefs(context);
    }

    public static SharedPrefs getInstance() {
        if (mSharedPrefs == null) {
            throw new RuntimeException("Have You missed initializing the object ?");
        }
        return mSharedPrefs;
    }

    public void add(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        }
        else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        else {
            Logger.e(TAG, "Value not inserted, Type " + value.getClass() + " not supported");
        }
        editor.commit();
    }

    /**
     * The default value defines what type of value is to be retrieved.
     * @param key - The key to look for.
     * @param defValue - The default value of no result was found.
     * @return
     */
    public Object get(String key, Object defValue) {
        if (defValue instanceof String) {
            return sharedPrefs.getString(key, (String) defValue);
        }
        else if (defValue instanceof Boolean) {
            return sharedPrefs.getBoolean(key, (Boolean) defValue);
        }
        else if (defValue instanceof Integer) {
            return sharedPrefs.getInt(key, (Integer) defValue);
        }
        else {
            return null;
        }
    }

    public void resetSharedPrefsValue() {
        Logger.d(TAG, "Resetting shared preference");
        editor.clear();
        editor.commit();
    }
}
