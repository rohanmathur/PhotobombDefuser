package org.on.puz.photobombsquad;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity2 extends Activity {

public final static String EXTRA_MESSAGE = "com.photobomb.detector.MainActivity2.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }
    
    /**
     * Called when the user clicks the Replacements button
     */
    public void startNickCage(View view) {
        Intent intent = new Intent(this, FdActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "NickCage");
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Remove button
     */
    public void startTrollFace(View view) {
        Intent intent = new Intent(this, FdActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "TrollFace");
        startActivity(intent);
    }

}
