package rat.rata2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class SocketManager {
    private Socket mySocket;

    private DataOutputStream bufferEscritura;
    private BufferedReader bufferLectura;

    public SocketManager(Socket sock) throws IOException {
        this.mySocket = sock;
        InicializaStreams();
       
    }

    /**
     * @param address InetAddress
     * @param port int numero de puerto
     * @throws java.io.IOException
     */
    public SocketManager(InetAddress address, int port) throws IOException {
        mySocket = new Socket(address, port);
        InicializaStreams();
        
    }

    /**
     * @param host String nombre del servidor al que se conecta
     * @param port int puerto de conexion
     * @throws java.io.IOException
     */
    public SocketManager(String host, int port) throws IOException {
        mySocket = new Socket(host, port);
        InicializaStreams();
    }

    /**
     * Inicializacion de los bufferes de lectura y escritura del socket
     * @throws java.io.IOException
     */
    public void InicializaStreams() throws IOException {
        bufferEscritura = new DataOutputStream(mySocket.getOutputStream());
        bufferLectura = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
        Log.i("a", "canales creados");
    }

    public void CerrarStreams() throws IOException {
        bufferEscritura.close();
        bufferLectura.close();
        Log.i("a", "canales creados");
    }

    public void CerrarSocket() throws IOException {
        mySocket.close();
    }

    /**
     * @return String
     * @throws java.io.IOException
     * Metodos de entrada salida del los sockets
     */
    public String Leer() throws IOException {
        return (
                bufferLectura.readLine());
    }

    public void Escribir(String contenido) throws IOException {

        bufferEscritura.writeBytes(contenido);
    }

    public void Escribir(byte[] buffer, int bytes) throws IOException {

        bufferEscritura.write(buffer, 0, bytes);
    }

    //Estos metodos los he conseguido de aqui http://stackoverflow.com/questions/2878867/how-to-send-an-array-of-bytes-over-a-tcp-connection-java-programming
    public void sendBytes(byte[] myByteArray) throws IOException {
        sendBytes(myByteArray, myByteArray.length);
    }

    void sendBytes(byte[] myByteArray, int len) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (0 >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + 0);
        // Other checks if needed.

        // just like the socket variable.
        // May be better to save the streams in the support class;
        OutputStream out = mySocket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, 0, len);
        }
    }
    public byte[] readBytes() throws IOException {
        // Again, probably better to store these objects references in the support class
        InputStream in = mySocket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }
        return data;
    }
}
