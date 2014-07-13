
package com.hd.snscoins.db;

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

public class SnsFixtureDataReader {
    Context context;

    private static List<String> latLongs;
    private static List<String> farmersNames;
    private static List<String> retailersNames;
    private static List<String> distributorsNames;
    private static List<String> productNames;
    private static List<String> mobileTypes;
    private static List<String> videoNames;
    private static List<String> geographies;
    private static List<String> geographiesTypes;
    private static List<String> retailerTypes;
    private static List<String> crops;
    private static List<String> locations;

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
        //readFromFile(context, R.raw.videos_new, getVideos());
    }

    private static void readFromFile(Context context, int id, List<String> listToAddValues) {
        try {
            InputStream inputStream = context.getResources().openRawResource(id);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    listToAddValues.add(receiveString);
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

    public synchronized static Vector<String> getFarmers() {
        if (farmersNames == null) {
            farmersNames = new Vector<String>();
        }
        return (Vector<String>) farmersNames;
    }

    public synchronized static Vector<String> getDistributors() {
        if (distributorsNames == null) {
            distributorsNames = new Vector<String>();
        }
        return (Vector<String>) distributorsNames;
    }

    public synchronized static Vector<String> getRetailersNames() {
        if (retailersNames == null) {
            retailersNames = new Vector<String>();
        }
        return (Vector<String>) retailersNames;
    }

    public synchronized static Vector<String> getLatLongs() {
        if (latLongs == null) {
            latLongs = new Vector<String>();
        }
        return (Vector<String>) latLongs;
    }

    public synchronized static List<String> getProductNames() {
        if (productNames == null) {
            productNames = new Vector<String>();
        }
        return productNames;
    }

    public static List<String> getMobileTypes() {
        if (mobileTypes == null) {
            mobileTypes = new Vector<String>();
        }
        return mobileTypes;
    }

    public static List<String> getVideos() {
        if (videoNames == null) {
            videoNames = new Vector<String>();
        }
        return videoNames;
    }

    public static List<String> getGeo() {
        if (geographies == null) {
            geographies = new Vector<String>();
        }
        return geographies;
    }

    public static List<String> getGeoTypes() {
        if (geographiesTypes == null) {
            geographiesTypes = new Vector<String>();
        }
        return geographiesTypes;
    }

    public static List<String> getRetailerTypes() {
        if (retailerTypes == null) {
            retailerTypes = new Vector<String>();
        }
        return retailerTypes;
    }

    public static List<String> getCrops() {
        if (crops == null) {
            crops = new Vector<String>();
        }
        return crops;
    }

    public static List<String> getLocations() {
        if (locations == null) {
            locations = new Vector<String>();
        }
        return locations;
    }
}
