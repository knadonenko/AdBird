package test.com.adbirdads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;



//import com.example.adbird.Classes.AdBirdContentProvider;
//import com.example.adbird.Classes.DatabaseHandler;
//import com.example.adbird.Classes.TypefaceSpan;

public class Themes extends Utilities implements OnTaskCompleted {

	private ThemesUpdateTask mAuthTask = null;
	private String mEmail;
	private String mPassword;
	private ContentResolver resolver;
	public static Boolean loaded = false;
	// themes
	private String themeName;
	private String themeSelected;
	private String themeId;
	private ArrayList<Integer> themeIds;

	int[] themesUIViewList;
	ArrayList<String> themesArray = new ArrayList<String>();
	ArrayList<CheckBox> cb = new ArrayList<CheckBox>();
	private String language;
	private int cbChecked1 = -1;
	private int cbChecked2 = -1;
	static final int PROFILE_PAGE_REQUEST = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_themes);

		// setting action bar's font
		String themeTitle = getString(R.string.title_activity_themes);
		SpannableString s = new SpannableString(themeTitle);
		s.setSpan(new TypefaceSpan(this, "GothaProReg.otf"), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActionBar().setTitle(s);
		// back button implementation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Locale current = getResources().getConfiguration().locale;
		language = current.toString();
		ImageView button = (ImageView) findViewById(R.id.imageView1);

		if (language.equals("ru")) {
			button.setBackgroundResource(R.drawable.save_changes_blue_ru);
		} else {
			button.setBackgroundResource(R.drawable.save_changes_blue);
		}
		viewBinderThemes();
		@SuppressWarnings("static-access")
		SharedPreferences prefs = getApplicationContext().getSharedPreferences(
				"prefs", getApplicationContext().MODE_PRIVATE);
		mEmail = prefs.getString("email", null);
		mPassword = prefs.getString("password", null);
		cb.add((CheckBox) findViewById(R.id.th_1));
		cb.add((CheckBox) findViewById(R.id.th_2));
		cb.add((CheckBox) findViewById(R.id.th_3));
		cb.add((CheckBox) findViewById(R.id.th_4));
		cb.add((CheckBox) findViewById(R.id.th_5));
		cb.add((CheckBox) findViewById(R.id.th_6));
		cb.add((CheckBox) findViewById(R.id.th_7));
		cb.add((CheckBox) findViewById(R.id.th_8));
		cb.add((CheckBox) findViewById(R.id.th_9));
		cb.add((CheckBox) findViewById(R.id.th_10));
		cb.add((CheckBox) findViewById(R.id.th_11));
		cb.add((CheckBox) findViewById(R.id.th_12));
		cb.add((CheckBox) findViewById(R.id.th_13));
		cb.add((CheckBox) findViewById(R.id.th_14));
		cb.add((CheckBox) findViewById(R.id.th_15));

		mAuthTask = new ThemesUpdateTask(this);
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getCheckedValues();

				if (isNetworkAvailable()) {
					mAuthTask.execute((Void) null);
				} else {
					String noInternetConn = getString(R.string.no_internet);
					Toast.makeText(getBaseContext(), noInternetConn,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	@Override
	public void onTaskCompleted() {
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateThemes() {
		ArrayList<Integer> cbIndexes = getCheckedValues();
		ArrayList<Integer> catIndexes = new ArrayList<Integer>();
		for (Integer cbIndex : cbIndexes) {
			catIndexes.add(themeIds.get(cbIndex));
		}

		int i = 0;
		for (@SuppressWarnings("unused")
		CheckBox box : cb) {
			int themeID = themeIds.get(i);
			ContentValues themeValues = new ContentValues();
			if (catIndexes.contains(themeID)) {
				themeValues.put(DatabaseHandler.KEY_SELECTED, "1");
			} else {
				themeValues.put(DatabaseHandler.KEY_SELECTED, "0");
			}
			resolver.update(AdBirdContentProvider.CONTENT_URI_THEMES,
					themeValues, DatabaseHandler.KEY_ID + "=?",
					new String[] { themeID + "" });
			i++;
		}
	}

	private void updateThemesNotAvail() {
		ContentValues themeValues = new ContentValues();
		themeValues.put(DatabaseHandler.KEY_THEME_UPDATE_AVAILABLE, "no");
		resolver.update(AdBirdContentProvider.CONTENT_URI_USER, themeValues,
				null, null);
	}

	private void viewBinderThemes() {
		;
		resolver = getApplication().getContentResolver();
		Locale current = getResources().getConfiguration().locale;
		String currentLang = current.toString();
		String themeLang = "";
		if (currentLang.equals("en")) {
			themeLang = DatabaseHandler.KEY_THEMES_NAME;
		} else {
			themeLang = DatabaseHandler.KEY_THEMES_NAME_RUS;
		}
		Cursor themesCursor = resolver.query(
				AdBirdContentProvider.CONTENT_URI_THEMES, new String[] {
						DatabaseHandler.KEY_ID, themeLang,
						DatabaseHandler.KEY_SELECTED }, null, null, null);

		themesCursor.moveToFirst();
		themesUIViewList = new int[] { R.id.th_1, R.id.th_2, R.id.th_3,
				R.id.th_4, R.id.th_5, R.id.th_6, R.id.th_7, R.id.th_8,
				R.id.th_9, R.id.th_10, R.id.th_11, R.id.th_12, R.id.th_13,
				R.id.th_14, R.id.th_15 };

		this.themeIds = new ArrayList<Integer>();
		if (themesCursor.moveToFirst()) {
			do {
				int themeIdIndex = themesCursor
						.getColumnIndex(DatabaseHandler.KEY_ID);
				int themeIdInt = Integer.parseInt(themesCursor
						.getString(themeIdIndex));
				themeIds.add(themeIdInt);
			} while (themesCursor.moveToNext());
		}
		Collections.sort(themeIds);

		if (themesCursor.moveToFirst()) {
			do {
				int themeNameIndex = themesCursor.getColumnIndex(themeLang);
				themeName = themesCursor.getString(themeNameIndex);
				int themeSelectedIndex = themesCursor
						.getColumnIndex(DatabaseHandler.KEY_SELECTED);
				themeSelected = themesCursor.getString(themeSelectedIndex);
				int themeIdIndex = themesCursor
						.getColumnIndex(DatabaseHandler.KEY_ID);
				themeId = themesCursor.getString(themeIdIndex);
				themeIdIndex = themeIds.indexOf(Integer.parseInt(themeId));
				CheckBox cb = (CheckBox) findViewById(themesUIViewList[themeIdIndex]);
				cb.setText(themeName);
				if (themeSelected.equals("0")) {
					cb.setChecked(false);
				} else if (themeSelected.equals("1")) {
					cb.setChecked(true);
					if (this.cbChecked1 == -1) {
						this.cbChecked1 = themeIdIndex;
					} else {
						this.cbChecked2 = themeIdIndex;
					}
				}

				findViewById(R.id.radGroup1).setVisibility(View.VISIBLE);
				findViewById(R.id.imageView1).setVisibility(View.VISIBLE);
			} while (themesCursor.moveToNext());
		}

	}

	public void onCheckboxClicked(View view) {
		checkBoxCount(view);
	}

	public void checkBoxCount(View v) {

		String max_themes = getString(R.string.max_themes);
		String min_theme = getString(R.string.min_theme);
		int numChecked = 0;
		int boxIndex = -1;
		int i = 0;
		for (CheckBox box : cb) {
			if (box.isChecked()) {
				numChecked++;
			}
			if (box == (CheckBox) v) {
				boxIndex = i;
			}
			i++;
		}
		if (numChecked == 0) {
			numChecked++;
			Toast.makeText(getBaseContext(), min_theme, Toast.LENGTH_SHORT)
					.show();
		}
		if (numChecked == 1) {
			if (boxIndex != this.cbChecked2) {
				this.cbChecked1 = boxIndex;
			}
			((CheckBox) v).setChecked(true);
		}
		if (numChecked == 2) {
			if (boxIndex != this.cbChecked1) {
				this.cbChecked2 = boxIndex;
			}
			((CheckBox) v).setChecked(true);
		}

		if (numChecked > 2) {
			Toast.makeText(getBaseContext(), max_themes, Toast.LENGTH_SHORT)
					.show();
			int j = 0;
			for (CheckBox box : cb) {
				if (j == this.cbChecked1) {
					box.setChecked(false);
				}
				j++;
			}
			if (boxIndex != this.cbChecked1 && boxIndex != this.cbChecked2) {
				this.cbChecked1 = this.cbChecked2;
				this.cbChecked2 = boxIndex;
			}
			((CheckBox) v).setChecked(true);
		}
	}

	public ArrayList<Integer> getCheckedValues() {
		ArrayList<Integer> checkedThemes = new ArrayList<Integer>();
		int i = 0;
		for (CheckBox box : cb) {
			if (box.isChecked()) {
				checkedThemes.add(i);
			}
			i++;
		}
		return checkedThemes;
	}

	public class ThemesUpdateTask extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog dialog = new ProgressDialog(Themes.this);
		private OnTaskCompleted listener;

		public ThemesUpdateTask(OnTaskCompleted listener) {
			this.listener = listener;
		}

		@Override
		protected void onPreExecute() {
			String msg = getString(R.string.please_wait);
			this.dialog.setMessage(msg);
			this.dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean success = false;
			try {
				// Simulate network access.
				try {
					success = getThemes();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}
			return success;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				updateThemes();
			}

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			listener.onTaskCompleted();
		}

		public Boolean getThemes() throws UnsupportedEncodingException,
				JSONException {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://api.adbird.net/index.php/surfer/setCategories");
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", mEmail));
			nameValuePairs.add(new BasicNameValuePair("password", mPassword));
			ArrayList<Integer> cbIndexes = getCheckedValues();
			ArrayList<Integer> catIndexes = new ArrayList<Integer>();
			for (Integer cbIndex : cbIndexes) {
				catIndexes.add(themeIds.get(cbIndex));
			}
			nameValuePairs.add(new BasicNameValuePair("usualCategories",
					catIndexes.toString()));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			try {
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String json = reader.readLine();
				@SuppressWarnings("unused")
				JSONArray jsonArray = null;
				JSONObject jsonObject = null;
				try {
					jsonArray = new JSONArray(json);

				} catch (Exception je) {
					jsonObject = new JSONObject(json);
				}

				if (jsonObject != null) {
					if (jsonObject.has("error")) {
						if ((jsonObject.getString("error"))
								.equals("themes update is not available")) {
							updateThemesNotAvail();
						}
						return false;
					} else if (jsonObject.has("success")) {
						return true;
					}
				} else {
					Log.d("themes", "fail " + json);
					return false;
				}
			} catch (IOException e) {
				Log.d("themes", "error execute");
				Log.d("themes", e.toString());
			}
			Log.d("themes", "response execute");
			return false;
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

}
