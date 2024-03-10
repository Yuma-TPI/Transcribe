package com.example.transcribe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer sr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mike permission
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            String[] permissions = {Manifest.permission.RECORD_AUDIO};
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        setRecognizer();
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Content","btnOn");
                startRecognizer();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.sr.destroy();
    }

    public void setRecognizer(){
        this.sr = SpeechRecognizer.createSpeechRecognizer(this);
        this.sr.setRecognitionListener(
                new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle bundle) {
                        Log.d("Content","onReadyForSpeech");
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        Log.d("Content","onBeginningOfSpeech");
                    }

                    @Override
                    public void onRmsChanged(float v) {
                        Log.d("Content","onRmsChanged");
                    }

                    @Override
                    public void onBufferReceived(byte[] bytes) {
                        Log.d("Content","onBufferReceived");
                    }

                    @Override
                    public void onEndOfSpeech() {
                        Log.d("Content","onEndOfSpeech");
                    }

                    @Override
                    public void onError(int i) {
                        Log.d("Content","onError ID: " + i);
                    }

                    @Override
                    public void onResults(Bundle bundle) {
                        String[] result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).toArray(new String[0]);
                        WebView wv = findViewById(R.id.wv);
                        wv.getSettings().setJavaScriptEnabled(true);
                        wv.loadUrl("https://www.google.co.jp/search?q=" + result[0]);
                        Log.d("Content","onResults");
                    }

                    @Override
                    public void onPartialResults(Bundle bundle) {
                        Log.d("Content","onPartialResults");
                    }

                    @Override
                    public void onEvent(int i, Bundle bundle) {
                        Log.d("Content","onEvent");
                    }
                }
        );
    }

    public void startRecognizer(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        this.sr.startListening(intent);
    }
}