package test.com.adbirdads;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HistoryFragment extends Fragment {

    private static final String GET_ADS = "http://api.adbird.net/index.php/post/history";
    static final String TAG_TITLE = "text";
    static final String TAG_PHOTO = "photo";
    static final String TAG_DATA = "postingDate";
    static final String TAG_PAYMENT = "payment";
    static final String TAG_STATUS = "status";
    static final String TAG_COMPANYNAME= "companyName";
    static final String TAG_COMMENTS= "comments";
    static final String TAG_LIKES= "likes";
    String photo, text, status, postingDate, payment, companyName, likes, comments;
    TextView noPosts;

    JSONArray results = null;

    private ListView adsListView;

    ArrayList<HashMap<String, String>> adsList;
    CustomHistoryAdapter adapter;

    SharedPreferences userPreferences;
    String email, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        new GetAds().execute();
        userPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        email = userPreferences.getString("email", null);
        password = userPreferences.getString("password", null);
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onStart() {
        adsList = new ArrayList<HashMap<String, String>>();
        adsListView = (ListView) getView().findViewById(R.id.listView);
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        noPosts = (TextView) getView().findViewById(R.id.noPosts);
    }

    public class GetAds extends AsyncTask<String, String, String> {

        private JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;

        @Override
        protected String doInBackground(String... strings) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            jsonArray = jsonParser.makeArrayRequest(GET_ADS, "POST", params);

            Log.d("JSON", String.valueOf(jsonArray));

            try {

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    text = jsonObject.getString(TAG_TITLE);
                    photo = jsonObject.getString(TAG_PHOTO);
                    postingDate = jsonObject.getString(TAG_DATA);
                    //payment = jsonObject.getString(TAG_PAYMENT);
                    status = jsonObject.getString(TAG_STATUS);
                    companyName = jsonObject.getString(TAG_COMPANYNAME);
                    likes = jsonObject.getString(TAG_LIKES);
                    comments = jsonObject.getString(TAG_COMMENTS);


                    postingDate.replace("-", ".");
                    String[] parts = new String[2];
                    parts = postingDate.split(" ");
                    postingDate = parts[0];

                    photo = photo.replace("\\/", "/");

                    Log.d("PHOTO URL", photo);
                    Log.d("TEXT", text);

                    map.put(TAG_TITLE, text);
                    map.put(TAG_DATA, postingDate);
                    //map.put(TAG_PAYMENT, payment);
                    map.put(TAG_STATUS, status);
                    map.put(TAG_PHOTO, "http://customer.adbird.kz" + photo);
                    map.put(TAG_COMPANYNAME, companyName);
                    map.put(TAG_LIKES, likes);
                    map.put(TAG_COMMENTS, comments);
                    adsList.add(map);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (jsonArray.length() > 0) {
                adapter = new CustomHistoryAdapter(getActivity(), adsList);
                adsListView.setAdapter(adapter);
            }
            else {
                noPosts.setVisibility(View.VISIBLE);
            }

        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public void showNoNetwork(){
        Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}
