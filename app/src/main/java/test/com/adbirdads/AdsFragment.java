package test.com.adbirdads;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.*;
import android.widget.AbsListView;
import android.widget.GridView;
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

public class AdsFragment extends Fragment {

    private static final String GET_ADS = "http://api.adbird.net/index.php/offer/showToSurfer";
    static final String email = "";
    static final String password = "";
    static final String TAG_TITLE = "text";
    static final String TAG_PHOTO = "photo";
    static final String TAG_BRAND = "brandName";
    static final String TAG_ID = "id";
    static final String TAG_DATE = "until_date";
    static final String TAG_AMOUNT="amount";
    String photo, text, post_id, date, brand, money;
    String instaLogin;
    static Boolean posted;
    static long time;
    TextView noPosts;
    int pageNumber;



    JSONArray results = null;

    private GridView adsListView;

    ArrayList<HashMap<String, String>> adsList;
    CustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String instagram = userPrefs.getString("instagramLogin", "");
        String Avatar = userPrefs.getString("avatar", null);
        Log.d("instagram", instagram);
        return inflater.inflate(R.layout.fragment_ads2, container, false);



    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
//        adsListView.setOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
//                if(i+i2>=i3){
//                    new GetAds().execute();
//                }
//            }
//        });
        getAdsAction();
    }

    public void getAdsAction() {
        pageNumber=1;
        new GetAds().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        noPosts = (TextView) getView().findViewById(R.id.noPosts);
        adsList = new ArrayList<HashMap<String, String>>();
        adsListView = (GridView) getView().findViewById(R.id.adsListView);
        getActivity().registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));

       // Log.i("REGISTERED", "Registered broadcast receiver");

        /*((PullToRefresh_Master) adsListView).setOnRefreshListener(new PullToRefresh_Master.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                adsList.clear();
                new GetAds().execute();
            }
        });*/


    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            setTime(millisUntilFinished);
          //  Log.i("COUNTDOWN", "Countdown seconds remaining: " +  millisUntilFinished / 1000);
            int secs = (int) (millisUntilFinished / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            //button.setText(" " + mins + " : " + String.format("%02d", secs));
            Boolean finished = intent.getBooleanExtra("finished", false);
            posted = intent.getBooleanExtra("posted", false);

            //Log.i("FINISHED?", String.valueOf(finished));
            //Log.i("POSTED", String.valueOf(posted));

            setPosted(posted);

            adapter.notifyDataSetChanged();

            if (finished == true) {
                getActivity().stopService(new Intent(getActivity(), BroadcastService.class));
            }
        }
    }

    public static void setTime(long milliseconds) {
        time = milliseconds;
    }

    public static long getTime() {
        return time;
    }

    public static void setPosted(boolean post) {
        posted = post;
    }

    public static boolean getPosted() {
        if (posted == null) {
            return posted = false;
        } else {
            return posted;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(br);
        Log.i("Unregistered", "Unregistered broadcast receiver");
    }

    @Override
    public void onStop() {
        try {
            getActivity().unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), BroadcastService.class));
        Log.i("STOPPED", "Stopped service");
        super.onDestroy();
    }

    public class GetAds extends AsyncTask<String, String, String> {

        private JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog = new ProgressDialog(getActivity());
           progressDialog.setMessage(getString(R.string.loading));
           progressDialog.setIndeterminate(false);
           progressDialog.setCancelable(true);
           progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String email = userPrefs.getString("email", null);
            String password = userPrefs.getString("password", null);
            String instagram = userPrefs.getString("instagramLogin", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("login", instagram));
            params.add(new BasicNameValuePair("stId", "1"));
           params.add(new BasicNameValuePair("page", pageNumber+""));

                jsonArray = jsonParser.makeArrayRequest(GET_ADS, "POST", params);

                Log.d("JSON", String.valueOf(jsonArray));
                Log.d("email", email);
                Log.d("password", password);
                Log.d("instagram", instagram);


                try {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        text = jsonObject.getString(TAG_TITLE);
                        photo = jsonObject.getString(TAG_PHOTO);
                        post_id = jsonObject.getString(TAG_ID);
                        date = jsonObject.getString(TAG_DATE);
                        brand = jsonObject.getString(TAG_BRAND);
                        money = jsonObject.getString(TAG_AMOUNT);
                        Log.d("MONEY", money);
                        Log.d("brand", brand);

                        photo = photo.replace("\\/", "/");

                        map.put(TAG_ID, post_id);
                        map.put(TAG_TITLE, text);
                        map.put(TAG_PHOTO, "http://customer.adbird.kz" + photo);
                        map.put(TAG_DATE, date);
                        map.put(TAG_BRAND, brand);
                        map.put(TAG_AMOUNT, money);

                        adsList.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                pageNumber++;

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            if (jsonArray.length() > 0) {
                adapter = new CustomAdapter(getActivity(), adsList);
                adsListView.setAdapter(adapter);
            }
            else {
                noPosts.setVisibility(View.VISIBLE);
            }



            //((PullToRefresh_Master) adsListView).onRefreshComplete();

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ads_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_update) {

            adsList.clear();
            getAdsAction();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        item.setVisible(false);
                        Thread.sleep(1000);
                        item.setVisible(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
        return super.onOptionsItemSelected(item);
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