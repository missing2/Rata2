package Libreria;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import Cliente.ClientListener;
import Controlador.GPSPacket;

/**
 * Created by Vero on 19/05/2015.
 */
public class GPSListener {
    private Context ctx;
    private String provider;
    private LocationManager mlocManager;
    private LocationListener listener;
    private int channel ;
    private GPSPacket packet;

    public GPSListener(LocationListener c, String prov, int chan) {
        listener = c;
        provider = prov;
        channel = chan ;

        packet = new GPSPacket();

        mlocManager = (LocationManager) ((Context) c).getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates( prov, 0, 0, listener);
        //mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }

    public void stop() {
        if (mlocManager != null) {
            mlocManager.removeUpdates(listener);
        }
    }


    public byte[] encode(Location loc) {
        packet = new GPSPacket(loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getSpeed(), loc.getAccuracy());
        return packet.build();
    }

    public int getChannel()
    {
        return channel;
    }
    public GPSListener(ClientListener client, String provider, int chan) {

    }
}
