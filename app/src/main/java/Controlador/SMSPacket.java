package Controlador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Vero on 19/05/2015.
 */
public class SMSPacket implements Paquete, Serializable{
    private int id;
    private int thread_id;
    private String address;
    private int person;
    private long date;
    private int read;
    private int type;
    private String body;

    public SMSPacket(int id, int thid, String add, int person, long date, int read, String body, int type) {
        this.id = id;
        this.thread_id = thid;
        this.address = add;
        this.person = person;
        this.date = date;
        this.read = read;
        this.body = body;
        this.type = type;
    }

    @Override
    public byte[] build() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
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
            SMSPacket p = (SMSPacket) in.readObject();
            this.id = p.id;
            this.thread_id = p.thread_id;
            this.address = p.address;
            this.body = p.body;
            this.date = p.date;
            this.person = p.person;
            this.read = p.read;
            this.type = p.type;

        } catch (Exception e) {
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThread_id() {
        return thread_id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
