package Controlador;

import java.nio.ByteBuffer;

/**
 * Created by Vero on 19/05/2015.
 */
public class Transporte implements Paquete{

    private int totalLength;
    private int awaitedLength;
    private int localLength;
    private boolean last;
    private short NumSeq;
    private int channel;
    private byte data[];

    private int fillingPosition;

    public Transporte() {
        awaitedLength = 0;
        fillingPosition = 0;

    }

    public Transporte(int tdl, int ll, int channel, boolean last,
                           short nums, byte[] data) {
        this.totalLength = tdl;
        this.channel = channel;
        this.last = last;
        this.data = data;
        this.localLength = ll;
        this.NumSeq = nums;
    }

    public void parse(byte[] packet) {
        ByteBuffer b = ByteBuffer.wrap(packet);

        this.totalLength = b.getInt();
        this.localLength = b.getInt();

        byte checkLast = b.get();
        if (checkLast == (byte) 1)
            this.last = true;
        else
            this.last = false;

        this.NumSeq = b.getShort();
        this.channel = b.getInt();
        this.data = new byte[b.remaining()];
        b.get(data, 0, b.remaining());
    }
    public boolean parse(ByteBuffer buffer) throws Exception{


        totalLength = buffer.getInt();
        localLength = buffer.getInt();

        byte lst = buffer.get();
        if (lst == 1)
            last = true;
        else
            last = false;

        NumSeq = buffer.getShort();
        channel = buffer.getInt();
        if ((buffer.limit() - buffer.position()) < localLength) {

            dataFilling(buffer, buffer.limit() - buffer.position());

            return true;

        }
        else
        {
            // si hay espacio almacenamos cualquier paquete
            data = new byte[localLength];
            buffer.get(data, 0, data.length);
            return false;

        }

    }

    public boolean parseCompleter(ByteBuffer buffer) throws Exception{

        // Si el tamaño de los datos esperados excede el de la memoria intermedia
        if (buffer.limit() - buffer.position() < awaitedLength) {

            // Recuperamos el tamaño del buffer
            dataFilling(buffer, buffer.limit() - buffer.position());
            return true;
        }
        else {

            //sino recuperamos todo
            dataFilling(buffer, awaitedLength);
            return false;
        }

    }

    public void dataFilling(ByteBuffer buffer, int length) {

        if( data == null) data = new byte[localLength];

        buffer.get(data, fillingPosition, length);
        fillingPosition += length;
        awaitedLength = localLength - fillingPosition;

    }

    public byte[] build() {
        byte[] cmdToSend = new byte[Protocolo.HEADER_LENGTH_DATA + data.length];
        byte[] header = Protocolo.dataHeaderGenerator(this.totalLength,
                this.localLength, this.last, this.NumSeq, this.channel);
        System.arraycopy(header, 0, cmdToSend, 0, header.length);
        System.arraycopy(data, 0, cmdToSend, header.length, data.length);

        return cmdToSend;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getLocalLength() {
        return localLength;
    }

    public boolean isLast() {
        return last;
    }

    public short getNumSeq() {
        return NumSeq;
    }

    public int getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }


}
