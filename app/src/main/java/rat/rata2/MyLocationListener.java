package rat.rata2;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Mikel on 21/05/2015.
 */
public class MyLocationListener implements LocationListener{
   MainActivity mainActivity;

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
        String text = "Mi ubicacion es: " +  location.getLongitude()+""
                +location.getLatitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
