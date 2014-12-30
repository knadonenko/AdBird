package test.com.adbirdads;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.adbird.Classes.ImageAdapter;

public class Tutorial extends Utilities {

	public Boolean active = true;
	public String language = "";
	public int[] tuteTexts = null;
	public TextView tv = null;
    Button button;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		setupFontsGotham(findViewById(R.id.tuteText));
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        button = (Button) findViewById(R.id.buttonNext);

		tv = (TextView)findViewById(R.id.tuteText);
		
		SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPref.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}
		
		if(language.equals("ru")){
			tuteTexts = new int[] { R.string.tute_ad, R.string.tute_2,R.string.tute_history };
		}else{
			tuteTexts = new int[] { R.string.tute_ad, R.string.tute_2,R.string.tute_history };
		}
		
		tv.setText(tuteTexts[0]);
		
		ImageAdapter adapter = new ImageAdapter(this);
        language=getString(R.string.language);
		adapter.setLanguage(language);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			int[] radioButtons = new int[] { R.id.r0, R.id.r1, R.id.r2 };

			public void onPageScrollStateChanged(int state) {
				Log.d("tute", "swipe!");
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			public void onPageSelected(int position) {
				// Check if this is the page you want.
				tv.setText(tuteTexts[position]);
				RadioGroup rd = (RadioGroup) findViewById(R.id.radioGroupTute);
				rd.check(radioButtons[position]);
                Log.d("POSITION", String.valueOf(position));
                if (position == 2) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }

			}
		});
		viewPager.setAdapter(adapter);
		/*findViewById(R.id.x).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});*/
		
	}

    public void openRegister(View v) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String email = sp.getString("email", null);
        if(email != null) {
            onBackPressed();

        } else {
            Intent intent = new Intent(Tutorial.this, Register.class);
            startActivity(intent);
            finish();
        }
    }

	protected void onStart() {
		super.onStart();
	};

	protected void onRestart() {
		super.onRestart();
	};

	public void onResume() {
		super.onResume();
	};

	public void onPause() {
		super.onPause();
	};

	public void onStop() {
		super.onStop();
	};

	protected void onDestroy() {
		super.onDestroy();
	};
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public void showNoNetwork(){
        Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}


