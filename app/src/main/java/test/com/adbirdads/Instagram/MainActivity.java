package test.com.adbirdads.Instagram;

//import com.example.adbird.R;

//import com.example.adbird.Instagram.InstagramApp.OAuthAuthenticationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import test.com.adbirdads.R;

public class MainActivity extends Activity {

	private InstagramApp mApp;
	private Button btnConnect;
	private TextView tvSummary;
	private ResponseListener mResponseListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		mApp.setListener(listener);

		tvSummary = (TextView) findViewById(R.id.tvSummary);

		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mApp.hasAccessToken()) {
					final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Disconnect from Instagram?")
					.setCancelable(false)
					.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							mApp.resetAccessToken();
							btnConnect.setText("Connect");
							tvSummary.setText("Not connected");
						}
					}).setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
					final AlertDialog alert = builder.create();
					alert.show();
				} else {
					mApp.authorize();
//					Intent i = new Intent(getApplicationContext(),InstagramTest.class);
//					startActivityForResult(i, 92);
				}
			}
		});

		if (mApp.hasAccessToken()) {
			tvSummary.setText("Connected as " + mApp.getUserName());
			btnConnect.setText("Disconnect");
		}
		mResponseListener = new ResponseListener();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
			final String accessToken = extras.getString(EXTRA_ACCESS_TOKEN);
			tvSummary.setText(name);

		}
	}

	InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {

		}

		@Override
		public void onFail(String error) {
			//Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
		}
	};
}