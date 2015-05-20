package Controlador;

import java.nio.ByteBuffer;

/**
 * Created by Vero on 20/05/2015.
 */
public class CommandPacket implements Paquete{
    private short commande;
    private int targetChannel;
    private byte[] argument;

    public CommandPacket() {

    }

    public CommandPacket(short cmd, int targetChannel, byte[] arg) {
        this.commande = cmd;
        this.argument = arg;
        this.targetChannel = targetChannel;
    }
    @Override
    public byte[] build() {
        byte[] byteCmd = ByteBuffer.allocate(2).putShort(commande).array();
        byte[] byteTargChan = ByteBuffer.allocate(4).putInt(targetChannel).array();
        byte[] cmdToSend = new byte[byteCmd.length + byteTargChan.length + argument.length];

        System.arraycopy(byteCmd, 0, cmdToSend, 0, byteCmd.length);
        System.arraycopy(byteTargChan, 0, cmdToSend, byteCmd.length, byteTargChan.length);
        System.arraycopy(argument, 0, cmdToSend, byteCmd.length + byteTargChan.length, argument.length);

        return cmdToSend;
    }

    @Override
    public void parse(byte[] packet) {
        ByteBuffer b = ByteBuffer.wrap(packet);
        this.commande = b.getShort();
        this.targetChannel = b.getInt();
        this.argument = new byte[b.remaining()];
        b.get(argument, 0, b.remaining());

    }
    public void parse(ByteBuffer b) {
        this.commande = b.getShort();
        this.targetChannel = b.getInt();
        this.argument = new byte[b.remaining()];
        b.get(argument, 0, b.remaining());
    }
    public short getCommand() {
        return commande;
    }

    public byte[] getArguments() {
        return argument;
    }

    public int getTargetChannel() {
        return targetChannel;
    }
}
