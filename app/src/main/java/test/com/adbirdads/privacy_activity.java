package test.com.adbirdads;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import test.com.adbirdads.Instagram.ApplicationData;
import test.com.adbirdads.Instagram.InstagramApp;


public class privacy_activity extends Activity {
    private InstagramApp mApp;
    private ResponseListener mResponseListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.privacy_activity));
        setContentView(R.layout.activity_privacy);
        Button confirm=(Button)findViewById(R.id.button2);

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(listener);

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //mApp.resetAccessToken();
                mApp.authorize();
                SharedPreferences socialPreferences = PreferenceManager.getDefaultSharedPreferences(privacy_activity.this);
                SharedPreferences.Editor edit = socialPreferences.edit();
                edit.putString("socialID", "1");
                edit.commit();
            }
        });

        mResponseListener = new ResponseListener();
    }



    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.varundroid.instademo.responselistener");
        filter.addCategory("com.varundroid.instademo");
        registerReceiver(mResponseListener, filter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mResponseListener);
        super.onPause();
    }

    public class ResponseListener extends BroadcastReceiver {

        public static final String ACTION_RESPONSE = "com.varundroid.instademo.responselistener";
        public static final String EXTRA_NAME = "90293d69-2eae-4ccd-b36c-a8d0c4c1bec6";
        public static final String EXTRA_ACCESS_TOKEN = "bed6838a-65b0-44c9-ab91-ea404aa9eefc";

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String name = extras.getString(EXTRA_NAME);
            String accessToken = extras.getString(EXTRA_ACCESS_TOKEN);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor ed = preferences.edit();
            ed.putString("socialized", "yes");
            ed.commit();
            Intent i = new Intent(getApplicationContext(),Loading.class);
            startActivity(i);

        }
    }

    InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail(String error) {

        }
    };
}
