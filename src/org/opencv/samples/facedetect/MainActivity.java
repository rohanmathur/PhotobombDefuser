package org.opencv.samples.facedetect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

public final static String EXTRA_MESSAGE = "com.photobomb.detector.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user clicks the Nick Cage button
     */
    public void startNickCage(View view) {
        Intent intent = new Intent(this, FdActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "NickCage");
        startActivity(intent);
    }

    /**
     * Called when the user clicks the TrollFace button
     */
    public void startTrollFace(View view) {
        Intent intent = new Intent(this, FdActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "TrollFace");
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Replace button
     */
    public void startRemove(View view) {
        Intent intent = new Intent(this, FdActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "Replace");
        startActivity(intent);
    }

}
