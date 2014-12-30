package test.com.adbirdads;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Locale;


public class ChangeLanguageActivity extends Activity {

    Locale myLocale;
    RelativeLayout russianLayout, englishLayout;
    ImageView flagRu, flagEng;
    SharedPreferences sp;
    String localization;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.title_activity_change_language);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        localization = sp.getString("locale", "ru");
        editor = sp.edit();


        russianLayout = (RelativeLayout) findViewById(R.id.russianLayout);
        englishLayout = (RelativeLayout) findViewById(R.id.englishLayout);
        flagRu = (ImageView) findViewById(R.id.ruFlag);
        flagEng = (ImageView) findViewById(R.id.engFlag);
        localization=getString(R.string.locale);
        if (localization.equals("ru")) {
            if (flagRu.getVisibility() == View.GONE) {
                flagRu.setVisibility(View.VISIBLE);
                flagEng.setVisibility(View.GONE);
            }
        } else if (localization.equals("en")) {
            if (flagEng.getVisibility() == View.GONE) {
                flagEng.setVisibility(View.VISIBLE);
                flagRu.setVisibility(View.GONE);
            }
        }

        russianLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("ru");
                editor.putString("locale", "ru");
                editor.commit();
            }
        });

        englishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
                editor.putString("locale", "en");
                editor.commit();
            }
        });


    }

    public void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);

//        myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ChangeLanguageActivity.this, NSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(ChangeLanguageActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ChangeLanguageActivity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("email");
            editor.remove("password");
            editor.commit();
            Intent intent = new Intent(ChangeLanguageActivity.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
