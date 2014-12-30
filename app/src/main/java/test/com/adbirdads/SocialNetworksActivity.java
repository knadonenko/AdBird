package test.com.adbirdads;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import test.com.adbirdads.Instagram.ApplicationData;
import test.com.adbirdads.Instagram.InstagramApp;


public class SocialNetworksActivity extends Activity{

	//public ImageView next = null;
	public RelativeLayout instaLayout = null;
	public TextView instagramLoginText = null;


	public String language = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_networks_testing);

		ActionBar action_bar = getActionBar();
		action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

		SharedPreferences myPr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPr.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}

		//instagramLoginText = (TextView)this.findViewById(R.id.textView2);

		instaLayout = (RelativeLayout)this.findViewById(R.id.relativeLayout2);
		
		//next = (ImageView)this.findViewById(R.id.button1);
		//next.setVisibility(View.GONE);




		instaLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//mApp.resetAccessToken();
				startActivity(new Intent(SocialNetworksActivity.this, privacy_activity.class));
			}
		});

		//mResponseListener = new ResponseListener();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {

		super.onPause();
	}
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
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