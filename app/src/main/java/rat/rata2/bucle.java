package rat.rata2;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class bucle extends AsyncTask<Void, Integer, Boolean> {// cosas de entrada, forma que voy a indicar el progreso, lo que devuelvo
    SocketManager socket;
    String resp;
    String ant="ant";

    //Se declara un buffer de lectura del dato escrito por el usuario por teclado
    //es necesario pq no es un buffer propio de los sockets
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
              socket = new  SocketManager("127.0.0.1", 2345);
                Log.i("socket creado", "bucle");
            } catch (IOException e) {
                Log.i("socket no creado fallo", "bucle");
                e.printStackTrace();
            }
            while(true){ // bucle infinito que estara escuchando peticiones

                try {
                    resp = socket.Leer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("respuesta es ",resp);
                   // if(resp != ant){
                       // aqui proceso el resp si hay algo
                       // if (resp==""){
                            // opcion 1
                      //  }else if(resp==""){
                            // opcion 2
                     //   }else{
                            // opcion 3
                      //  }

                    resp=ant; //reinicio la variable para que este escuchando otra vez

                return true;
            }


        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean result) {
            try {
                socket.CerrarSocket(); // cierro socket cuando cierro la app
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {

        }

}
