package rat.rata2;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public void EscribirBytes(byte[] buffer, int bytes) throws IOException{
    	
    	bufferEscritura = new DataOutputStream(mySocket.getOutputStream());
    	bufferEscritura.writeInt(bytes);
    	if(bytes > 0)
    		bufferEscritura.write(buffer, 0, bytes);
    }
    public byte[] LeerBytes() throws IOException{
    	
		DataInputStream dis = new DataInputStream(mySocket.getInputStream());

		int bytes = dis.readInt();
		System.out.println(bytes);
		byte[] data = new byte[bytes];
        Log.i("a", "leer");
		if (bytes > 0) {
			dis.readFully(data);
		}
		return data;
    	
    }
}
