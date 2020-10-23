package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static android.Manifest.permission.RECORD_AUDIO;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "SpeechRecognizer";
    private SpeechRecognizer recognizer;
    private Button btnStart;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = (Button) this.findViewById(R.id.button12);
        textView2 = (TextView) this.findViewById(R.id.speechRecognizer_activity_txv);
        if(checkPermissions()) {
            init();
        }
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                recognizer.startListening(intent);
            }
        });
    }

    public void init(){
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(new SpeechRecognizerListener());
    }

    public class SpeechRecognizerListener implements RecognitionListener{

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {

            List resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            StringBuffer sb = new StringBuffer();
            for(Object res: resList) {
                sb.append(res + "\n");
            }
            textView2.setText("onResults: \n" + sb.toString());
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

    public void start(View v){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizer.startListening(intent);

    }

    @Override
    protected void onDestroy() {
        recognizer.stopListening();
        recognizer.destroy();
        super.onDestroy();
    }

    public Boolean checkPermissions(){

        if(Build.VERSION.SDK_INT >=23) {

            int pm = ActivityCompat.checkSelfPermission(this, RECORD_AUDIO);
            if (pm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO}, 1);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case 1:
                if (grantResults.length > 0){

                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG,permissions[0] +"allow");
                        init();
                    }
                    else{
                        checkPermissions();
                    }
                } else {
                    Log.d(TAG,"no pm allow");
                }

                return;
        }
    }
}