package Libreria;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import rat.rata2.MainActivity;

/**
 * Created by Vero on 19/05/2015.
 */
public class GPSListener implements LocationListener{

    private LocationManager mlocManager;
    private LocationListener listener;
    public double longitud;
    public double alt;
    String text;
    MainActivity ma;

    public GPSListener() {
    }

    @Override
    public void onLocationChanged(Location location) {
       longitud = location.getLatitude();
       alt = location.getLongitude();
       text = "Mi ubicacion es: " + longitud+""+alt;
       ma.setLocation(location);
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
