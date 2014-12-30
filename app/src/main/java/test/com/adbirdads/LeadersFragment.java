package test.com.adbirdads;



import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class LeadersFragment extends Fragment {

    static final String TAG_LEADERNAME = "name";
    static final String TAG_LEADERSURNAME = "surname";
    static final String TAG_LEADERRATING = "rating";
    static final String TAG_PHOTO = "avatar";

    private ListView leadersListView;

    ArrayList<HashMap<String, String>> adsList;
    LeadrsAdapter adapter;


    public LeadersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new LeadersTask().execute();
        getActivity().getActionBar().setTitle(R.string.leaders);
        return inflater.inflate(R.layout.fragment_leaders, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        leadersListView = (ListView) getView().findViewById(R.id.listView);

        adsList = new ArrayList<HashMap<String, String>>();
    }

    public class LeadersTask extends AsyncTask {
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        String URL = "http://api.adbird.net/index.php/surfer/liders";

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String email = sharedPreferences.getString("email", null);
            String password = sharedPreferences.getString("password", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));

            jsonArray = jsonParser.makeArrayRequest(URL, "POST", params);

            Log.d("JSONLeaders", jsonArray.toString());


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {

                if (jsonArray.length() > 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        String name = jsonObject.getString(TAG_LEADERNAME);
                        String surname = jsonObject.getString(TAG_LEADERSURNAME);
                        String rating = jsonObject.getString(TAG_LEADERRATING);
                        String photo = jsonObject.getString(TAG_PHOTO);
                        //post_id = jsonObject.getString(TAG_ID);

                        if (!photo.equals("null")) {
                            photo = photo.replace("\\/", "/");
                            map.put(TAG_PHOTO, photo);
                            Log.d("PHOTO", photo);
                        }

                        map.put(TAG_LEADERNAME, name);
                        map.put(TAG_LEADERSURNAME, surname);

                        map.put(TAG_LEADERRATING, rating);
                        //map.put(TAG_BRAND, brand);

                        adsList.add(map);

                        //Log.d("LEADERS", adsList.toString());
                    }
                } else {
                    //noPosts.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter = new LeadrsAdapter(getActivity(), adsList);
            leadersListView.setAdapter(adapter);

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
