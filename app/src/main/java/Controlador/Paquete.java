package Controlador;

/**
 * Created by Vero on 19/05/2015.
 */
public interface Paquete {
    public byte[] build();

    public void parse(byte[] packet);
}
