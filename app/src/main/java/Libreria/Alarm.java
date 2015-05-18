package Libreria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import rat.rata2.client;

/**
 * Created by Vero on 14/05/2015.
 */
public class Alarm extends BroadcastReceiver {
    public final String TAG = Alarm.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "Alarma recibida !");
            try {
                Bundle bundle = intent.getExtras();
                String message = bundle.getString("alarm_message");
                if(message != null) {
                    Log.i(TAG, "Message received: "+message);

                    Intent serviceIntent = new Intent(context, client.class);
                    serviceIntent.setAction(Alarm.class.getSimpleName());//By this way the Client will know that it was AlarmListener that launched it
                    context.startService(serviceIntent);

                }

            } catch (Exception e) {
                Log.e(TAG, "Error!!!"+ e.getMessage());
            }
    }
}
