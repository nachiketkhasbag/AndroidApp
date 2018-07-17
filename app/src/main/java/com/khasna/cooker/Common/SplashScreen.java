package com.khasna.cooker.Common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.khasna.cooker.R;

/**
 * Created by nachiket on 3/29/2017.
 */

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);

                            // close this activity
                            finish();
                    }
                // ...
                }, SPLASH_TIME_OUT);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

