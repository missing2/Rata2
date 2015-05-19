package Controlador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Vero on 19/05/2015.
 */
public class SMSTreePacket implements Paquete{
    ArrayList<SMSPacket> list;
    public SMSTreePacket(ArrayList<SMSPacket> l) {
        list = l;
    }


    @Override
    public byte[] build() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(list);
            return bos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void parse(byte[] packet) {
        ByteArrayInputStream bis = new ByteArrayInputStream(packet);
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(bis);
            list = (ArrayList<SMSPacket>) in.readObject();
        } catch (Exception e) {
        }
    }
    public ArrayList<SMSPacket> getList() {
        return list;
    }
}
