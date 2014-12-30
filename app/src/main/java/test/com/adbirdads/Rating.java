package test.com.adbirdads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Rating extends Utilities {

	//private String rating;
	@SuppressWarnings("unused")
	private String email;
	@SuppressWarnings("unused")
	private String password;
	private String level;
	public String language = null;
    String rating;
    TextView moneyTextView;
    static final String TAG_LEADERNAME = "name";
    static final String TAG_LEADERSURNAME = "surname";
    static final String TAG_LEADERRATING = "rating";
    static final String TAG_PHOTO = "avatar";
    ArrayList<HashMap<String, String>> adsList;
    RatingAdapter adapter;
   public ListView topsList;
    SharedPreferences ratingPrefs;
    TextView username, ratingTV, ratingBig;
    ImageView myAva;
    ImageLoader imageLoader;
    RelativeLayout ney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPref.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}*/

		//SharedPreferences prefsForUserDetails = getSharedPreferences("prefs", MODE_PRIVATE);
		//email = prefsForUserDetails.getString("email", null);
		//password = prefsForUserDetails.getString("password", null);

//        SharedPreferences userPrefs = PreferenceManager
//                .getDefaultSharedPreferences(Rating.this);
//        if(userPrefs.getBoolean("alreadyREG", false)){
//            Intent intentLoading = new Intent(getBaseContext(),
//                    MainActivity.class);
//            startActivity(intentLoading);
//        }
//        email = userPrefs.getString("email", null);
//        password = userPrefs.getString("password", null);

		setContentView(R.layout.activity_rating);
        username=(TextView)findViewById(R.id.username);
        ratingTV=(TextView)findViewById(R.id.Rating);
        myAva=(ImageView)findViewById(R.id.leader_avatar);
        ney=(RelativeLayout)findViewById(R.id.neymar);
        ratingBig=(TextView)findViewById(R.id.rating_evaluation);
        adsList = new ArrayList<HashMap<String, String>>();
        imageLoader = new ImageLoader(getApplicationContext());
		//setupFontsGotham(findViewById(R.id.user_level));
		//setupFontsHelLight(findViewById(R.id.rating_evaluation));
        new RatingTask().execute();
		SharedPreferences prefs = getSharedPreferences("rating", MODE_PRIVATE);
		rating = prefs.getString("rating", "0");
		level = prefs.getString("level", null);

		TextView rating_eval = (TextView) findViewById(R.id.rating_evaluation);

        topsList=(ListView)findViewById(R.id.rating_list);


		
//		double d = Double.parseDouble(rating);
//
//        String locale = Locale.getDefault().getDisplayLanguage();
//
//        if (locale.equals("русский")) {
//
//            //rating_eval.setText(ratingEvalString+" "+(int)d+" тг."+ratingContinuationString);
//            rating_eval.setText(String.format("%.0f", d));
//            moneyTextView.setText(getResources().getString(R.string.youearn) +
//                    " " + String.format("%.0f", d) + " тг.");
//        } else {
//            rating_eval.setText(String.format("%.0f", d));
//            moneyTextView.setText(getResources().getString(R.string.youearn) +
//                    " " + String.format("%.0f", d) + " tenge per ad.");
//        }
//        String saveRating = String.format("%.0f", d);
//
//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("rating", saveRating);
//
		//ImageView ratingScore = (ImageView) findViewById(R.id.rating_num);

		Button button = (Button) findViewById(R.id.next_button);

        button.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intentLoading = new Intent(getBaseContext(),
								CategoriesActivity.class);
						startActivity(intentLoading);
					}
				});
		

	}

	@Override
	public void onBackPressed() {
	}

	protected void onStart(){
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
	protected void onDestroy(){
		super.onDestroy();
	};

    public class RatingTask extends AsyncTask {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        JSONObject jsonObjectt;
        String URL = "http://api.adbird.net/index.php/surfer/getRating";

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Rating.this);
            String email = sharedPreferences.getString("email", null);
            String password = sharedPreferences.getString("password", null);
            String login=sharedPreferences.getString("instagramLogin", null);
           // Log.d("LOGIN", sharedPreferences.getString("instagramLogin", "lol"));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("stId", "1"));
            params.add(new BasicNameValuePair("login", login));

            jsonObjectt = jsonParser.makeHttpRequest(URL, "POST", params);

            Log.d("JSONRatings", jsonObjectt.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {

                if (jsonObjectt.getString("scanSuccess").equals("1")) {
                    rating=jsonObjectt.getString("rating");
                    ratingBig.setText(rating);
                    jsonArray=jsonObjectt.getJSONArray("topsurfers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String name = jsonObject.getString(TAG_LEADERNAME);
                        String surname = jsonObject.getString(TAG_LEADERSURNAME);
                        String rating = jsonObject.getString(TAG_LEADERRATING);
                        String photo = jsonObject.getString(TAG_PHOTO);
                        String login=jsonObject.getString("login");

                        //post_id = jsonObject.getString(TAG_ID);

                        if (!photo.equals("null")) {
                            photo = photo.replace("\\/", "/");
                            map.put(TAG_PHOTO, photo);
                            Log.d("PHOTO", photo);
                        }

                        map.put(TAG_LEADERNAME, name);
                        map.put(TAG_LEADERSURNAME, surname);

                        map.put(TAG_LEADERRATING, rating);
                        map.put("login", login);
                        //map.put(TAG_BRAND, brand);

                        adsList.add(map);

                        //Log.d("LEADERS", adsList.toString());
                    }
                    if(jsonObjectt.has("placenumber")){
                    JSONObject JSONme=jsonObjectt.getJSONObject("placenumber");
                    username.setText(JSONme.getString("name")+" "+JSONme.getString("surname"));
                    ratingTV.setText(JSONme.getString("rating"));
                    String photo = JSONme.getString(TAG_PHOTO);
                    if (!photo.equals("null")) {
                        photo = photo.replace("\\/", "/");}
                    imageLoader.DisplayImage(photo,myAva);}else
                    {
                        ney.setVisibility(View.GONE);
                    }
                } else {
                    //noPosts.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new RatingAdapter(getApplicationContext(), adsList);
            topsList.setAdapter(adapter);

        }
    }

}
