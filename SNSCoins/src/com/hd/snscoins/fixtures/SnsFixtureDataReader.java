
package com.hd.snscoins.fixtures;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.util.Log;

import com.hd.snscoins.R;

class SnsFixtureDataReader {
    Context context;

    private static List<String> coins;
    private static List<String> coinTypes;
    private static List<String> coinSubTypes;

    private static SnsFixtureDataReader dataReader;

    /**
     * Always Initialies this Constructor in a background thread, this does an heavy opertion.
     * Running on UI thread might cause ANR.
     * @param context
     */
    private SnsFixtureDataReader(Context context) {
        super();
        this.context = context;
    }

    public static void init(Context context) {
        if (dataReader == null) {
            dataReader = new SnsFixtureDataReader(context);
        }
    }

    public static SnsFixtureDataReader getInstance() {
        if (dataReader == null) {
            throw new RuntimeException("The method inti has to be called before calling this method");
        }

        return dataReader;
    }

    public void initialDataReadingProcess() {
        readFromFile(context, R.raw.coins, getCoins());
        readFromFile(context, R.raw.coin_type, getCoinTypes());
        readFromFile(context, R.raw.coin_sub_type, getCoinSubTypes());
    }

    private static void readFromFile(Context context, int id, List<String> listToAddValues) {
        try {
            InputStream inputStream = context.getResources().openRawResource(id);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    if (receiveString != null && !receiveString.equals("")) {
                        listToAddValues.add(receiveString);
                    }
                }
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        }
        catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

    public static List<String> getCoins() {
        if (coins == null) {
            coins = new Vector<String>();
        }
        return coins;
    }

    public static List<String> getCoinTypes() {
        if (coinTypes == null) {
            coinTypes = new Vector<String>();
        }
        return coinTypes;
    }

    public static List<String> getCoinSubTypes() {
        if (coinSubTypes == null) {
            coinSubTypes = new Vector<String>();
        }
        return coinSubTypes;
    }

}
