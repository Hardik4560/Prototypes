
package com.hardy.utils;

import java.util.Date;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.hardy.logging.LogIt;

public class DeviceUtils {
    private static boolean macFound = true;
    private static boolean enabledWifiProg;
    public static final String STUFF_MAC_ADDRESS = "";

    public static final String STUFF_DEVICE_ID = "";
    public static Size size;

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static void enableWifiConnection(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            enabledWifiProg = true;
        }
    }

    public static String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wifiManager.getConnectionInfo().getMacAddress();
        return macAddress;
    }

    public static void disableWifiConnection(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static int getAPILevel() {
        return Integer.parseInt(Build.VERSION.SDK);
    }

    public static String getOsModel() {
        return Build.MODEL;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * appends the application is to the unique id.
     * 
     * @param id
     * @return
     */
    public static String uniqueIdGenerator(String id, Context context) {
        if (id == null) {
            boolean getDeviceId = false;

            /**
             * the mac address retrieved is null. This could be because of the
             * Wifi connection being disabled.
             */
            if (enabledWifiProg) {
                // if the wifi was intially enabled from the program.
                getDeviceId = true;
                macFound = false;
            }
            else {
                // enable the wifi connection programatically.
                if (!isWifiEnabled(context)) {
                    enableWifiConnection(context);

                    // again check for the mac address.
                    id = getMacAddress(context);
                    if (id == null) {
                        getDeviceId = true;
                        macFound = false;
                    }
                    else {
                        // got the mac address.Disable the wifi connection to
                        // get it back in a consistent state.
                        disableWifiConnection(context);
                    }
                }
                else {
                    // the wifi connection was already enabled.
                    // there is some problem in getting the mac address.
                    getDeviceId = true;
                    macFound = false;
                }
            }
            if (getDeviceId) {
                // the previous attempts to get the mac address have failed.
                // try to get the device id.(IMEI)
                id = getDeviceId(context);
                // check the retrieved device id for its length.
                if (id != null && id.length() <= 7) {
                    // not a valid device id
                    id = null;
                }
            }
        }
        return id;
    }

    private static String stuffMeWantsMac(String reversedString, String whatToStuff) {
        String stuffedString = "";
        String firstSixChars = reversedString.substring(0, 6);
        String afterFirstSixChars = reversedString.substring(6);

        stuffedString = firstSixChars + whatToStuff + afterFirstSixChars;
        return stuffedString;
    }

    private static String encodeUniqueId(String uniqueId) {
        String encodedUniqueId = "";
        // reverse the unique id.
        String reversedString = "";
        for (int i = uniqueId.length() - 1; i >= 0; i--) {
            reversedString += uniqueId.charAt(i);
        }
        if (macFound) {
            // the mac address was found.
            // stuff the reversedString with the String PIXE: after the 6th
            // character.
            encodedUniqueId = stuffMeWantsMac(reversedString, STUFF_MAC_ADDRESS);
        }
        else {
            // the device id was found
            // stuff the reversedString with the String :MEWANTS: after the 6th
            // character.
            encodedUniqueId = stuffMeWantsMac(reversedString, STUFF_DEVICE_ID);
        }
        return encodedUniqueId;
    }

    /**
     * Generates a unique id based on the device id.
     * 
     * @param mActivityContext
     * @return
     */
    public static String generateUniqueId(Context mActivityContext) {

        String uniqueId = uniqueIdGenerator(getMacAddress(mActivityContext), mActivityContext);

        // if the wifi was enabled programatically, disable it.
        if (enabledWifiProg) {
            disableWifiConnection(mActivityContext);
            enabledWifiProg = false;
        }

        /**
         * This is just for testing purposes, in order to make the device id
         * unique on an emulator also.
         */
        if (uniqueId != null) {
            if (uniqueId.equals("000000000000000")) {
                // which it will be on an emulator and the above would be the
                // device id that was found out
                uniqueId = Secure.getString(mActivityContext.getContentResolver(), Secure.ANDROID_ID);
                if (uniqueId == null) {
                    Date d = new Date();
                    uniqueId = d.getTime() + "";
                }
            }
            // encode the unique id
            uniqueId = encodeUniqueId(uniqueId);
        }
        return uniqueId;
    }

    public static Size getSize(Context context) {
        if (size == null) {
            int height = context.getResources().getDisplayMetrics().heightPixels;
            int width = context.getResources().getDisplayMetrics().widthPixels;
            size = new Size(width, height);
        }
        return size;
    }

    public static class Size {

        private static final String TAG = "Size";

        public Size(int width, int height) {
            super();
            this.width = width;
            this.height = height;
        }

        int width, height;

        public int getWidth() {
            LogIt.i(TAG, "Width: " + width);
            return width;
        }

        public int getHeight() {
            LogIt.i(TAG, "Height: " + height);
            return height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
