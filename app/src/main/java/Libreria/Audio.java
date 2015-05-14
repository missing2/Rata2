package Libreria;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;

import com.example.seginf.myapplication.R;

import java.io.IOException;

/**
 * Created by Vero on 06/05/2015.
 */
public class Audio extends Activity {
    private static final String LOG_TAG = "Audio";
    String outputFile = null;
    MediaRecorder recorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recorder = new MediaRecorder();
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/grabAudio.3gp";
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(outputFile);
    }
    public void start(){
        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stop(){
        recorder.stop();
        recorder.release();
    }

}
