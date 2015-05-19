package Cliente;

import android.content.Intent;
import android.content.SharedPreferences;

import java.nio.ByteBuffer;

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

    public ProcessCommand(ClientListener c)
    {
        this.client = c;
        settings = client.getSharedPreferences("preferences.xml", 0);
        editor = settings.edit();
    }

    public void process(short cmd, byte[] args, int chan)
    {
        this.comm = cmd;
        this.chan = chan;
        this.arguments = ByteBuffer.wrap(args);
}
