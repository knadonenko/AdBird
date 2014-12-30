package test.com.adbirdads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    Boolean firstTime;
    String email, password, registered, lang, socialized;
    private CountDownTimer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        timer=new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isNetworkAvailable()) {
                            showNoNetwork();
                            timer.start();

                        } else if (firstTime) {
                            Intent i = new Intent(SplashScreen.this, Tutorial.class);
                            startActivity(i);

                            finish();
                        } else if (email != null && socialized == null) {
                            Intent i = new Intent(SplashScreen.this, SocialNetworksActivity.class);
                            startActivity(i);

                            finish();
                        } else if ((email != null || password != null) && socialized != null) {
                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);

                            finish();
                        } else if (registered != null) {
                            Intent i = new Intent(SplashScreen.this, Login.class);
                            startActivity(i);

                            finish();
                        } else {
                            Intent i = new Intent(SplashScreen.this, Register.class);
                            startActivity(i);

                            finish();
                        }
                    }
                }, 2000);
            }
        };


        String locale = Locale.getDefault().getDisplayLanguage();
        if (locale.equals("русский")) {
            locale = "ru";
        } else if (locale.equals("English")) {
            locale = "en";
        }

        Log.d("Locale", locale);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // SharedPreferences.Editor editor = sp.edit();
        // editor.putString("locale", locale);
        // editor.commit();
        lang = sp.getString("locale", locale);
        Configuration config = new Configuration();
        config.locale = new Locale(lang);
        this.getApplicationContext().getResources().updateConfiguration(config, null);


        firstTime = sp.getBoolean("firstTime", true);
        email = sp.getString("email", null);
        password = sp.getString("password", null);
        registered = sp.getString("registered", null);
        socialized = sp.getString("socialized", null);

    timer.start();




    }




    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public void showNoNetwork(){
        Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}