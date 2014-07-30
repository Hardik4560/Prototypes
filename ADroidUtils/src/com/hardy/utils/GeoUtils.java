
package com.hardy.utils;

import java.util.List;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class GeoUtils {

    /**
     * Return the location based on the best service provider, this can be gps or signal triangulation.
     * @return
     */
    public static Location requestLocation(LocationManager lm, LocationListener locationListener) {
        if (lm == null || locationListener == null) {
            throw new RuntimeException("Passing null location manager or location listener won't help you get the location./n Have you missed assigning Location Manager or location listener");
        }
        Criteria criteria = new Criteria();
        String bestProvider = lm.getBestProvider(criteria, true);

        if (bestProvider == null) {
            bestProvider = LocationManager.GPS_PROVIDER;
        }
        lm.requestLocationUpdates(bestProvider, 60 * 1 * 1000, 100, locationListener);

        Location loc = getLastKnownLocation(lm);
        return loc;
    }

    private static Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            //Log.d("last known location, provider: %s, location: %s", provider, l);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                || l.getAccuracy() < bestLocation.getAccuracy()) {
                // ALog.d("found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }
}
