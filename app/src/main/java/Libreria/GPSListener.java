package Libreria;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Vero on 19/05/2015.
 */
public class GPSListener implements LocationListener{

    private LocationManager mlocManager;
    private LocationListener listener;
    public double longitud;
    public double alt;
    String text;

    public GPSListener(LocationListener c) {
        listener = c;

        mlocManager = (LocationManager) ((Context) c).getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, listener);
        //mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }

    @Override
    public void onLocationChanged(Location location) {
       longitud = location.getLatitude();
       alt = location.getLongitude();
       text = "Mi ubicacion es: " + longitud+""+alt;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        text = "GPS ON";
    }

    @Override
    public void onProviderDisabled(String provider) {
        text = "GPS DISABLED";

    }
}
