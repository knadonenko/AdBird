package test.com.adbirdads;

//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.parse.PushService;

import java.util.Locale;


public class NSettingsActivity extends Activity {

    TextView instalogin;
    CheckBox notificationsCheckBox;
    String emailParse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nsettings);
        getActionBar().setTitle(getString(R.string.settings_Activity));
        notificationsCheckBox = (CheckBox) findViewById(R.id.notificationsCheckBox);



		String settingsTitle = getString(R.string.title_activity_settings);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String isSubscribed = sp.getString("subscribed", "true");
        emailParse = sp.getString("email", null);

        if (isSubscribed.equals("true")) {
            notificationsCheckBox.setChecked(true);
        } else if(isSubscribed.equals("false")) {
            notificationsCheckBox.setChecked(false);
        }

        instalogin = (TextView) findViewById(R.id.instaLogin);
        instalogin.setText(sp.getString("instagramLogin", "anon"));

        notificationsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (notificationsCheckBox.isChecked()) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NSettingsActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("subscribed", "true");
                    editor.commit();
                    String replaceAt = emailParse.replace("@", "_");
                    String parseEmail = replaceAt.replace(".", "_");
                    PushService.subscribe(getApplicationContext(), parseEmail, NSettingsActivity.class);
                } else if(!notificationsCheckBox.isChecked()) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NSettingsActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("subscribed", "false");
                    editor.commit();
                    String replaceAt = emailParse.replace("@", "_");
                    String parseEmail = replaceAt.replace(".", "_");
                    PushService.unsubscribe(getApplicationContext(), parseEmail);
                }

            }
        });



		//SpannableString s = new SpannableString(settingsTitle);
		//s.setSpan(new TypefaceSpan(this, "GothaProReg.otf"), 0, s.length(),
		//		Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//getActionBar().setTitle(s);

        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //String localization = sp.getString("locale", "ru");

//        Locale locale = new Locale(localization);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        this.getApplicationContext().getResources().updateConfiguration(config, null);

	}

    public void openSocialSettings(View v) {
        Intent intent = new Intent(NSettingsActivity.this, SocialNetworksConfig.class);
        intent.putExtra("instagram", "instagram");
        startActivity(intent);
    }
	
	
	public void onResume() {
		//Tracker t = ((ParseApplication) getApplication()).getTracker(
	    //        TrackerName.APP_TRACKER);
	    //t.setScreenName("com.example.adbird.NSettingsActivity");
	    //t.send(new HitBuilders.AppViewBuilder().build());
		super.onResume();
	};

    public void changePassword(View v) {
        Intent intent = new Intent(NSettingsActivity.this, ChangePassword.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
           // Intent intent = new Intent(NSettingsActivity.this, NSettingsActivity.class);
           // startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(NSettingsActivity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NSettingsActivity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("email");
            editor.remove("password");
            editor.commit();
            Intent intent = new Intent(NSettingsActivity.this, Login.class);
            startActivity(intent);
            return true;
        }
        switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Intent intentMenu = new Intent(getBaseContext(),
					MainActivity.class);
			startActivity(intentMenu);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void notificationClicked(View v){
		Log.d("settings", "notificationClicked");
	}
	public void tutorialClicked(View v){
		Intent intentTutorial = new Intent(getBaseContext(),
				Tutorial.class);
		startActivity(intentTutorial);
	}

    public void changeLanguage(View v){
        Intent intentLang = new Intent(NSettingsActivity.this, ChangeLanguageActivity.class);
        startActivity(intentLang);
    }

	public void contactusClicked(View v){
		//Intent intentContactis = new Intent(getBaseContext(),
		//		FeedbackActivity.class);
		//startActivity(intentContactis);
	}
	public void languageClicked(View v){
		//Intent intentLang = new Intent(getBaseContext(),
		//		SettingsLang.class);
		//startActivity(intentLang);
	}
	
	public void privacyClicked(View v){
		//Intent intentPol = new Intent(getBaseContext(),
		//		PrivacyPolicy.class);
		//startActivity(intentPol);
	}
	public void logoutClicked(View v){
		getApplicationContext().getSharedPreferences("prefs",
				MODE_PRIVATE).edit().remove("email").commit();
		getApplicationContext().getSharedPreferences("prefs",
				MODE_PRIVATE).edit().remove("password").commit();
		getApplicationContext().getSharedPreferences("rating",
				MODE_PRIVATE).edit().remove("level").commit();
		getApplicationContext().getSharedPreferences("rating",
				MODE_PRIVATE).edit().remove("rating").commit();

		Intent intentLogin = new Intent(getBaseContext(),
				Login.class);
		startActivity(intentLogin);
	}

}
