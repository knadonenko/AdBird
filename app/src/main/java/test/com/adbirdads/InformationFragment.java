package test.com.adbirdads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.com.adbirdads.yandexlocator.PaymentActivity;

public class InformationFragment extends Fragment {

    String name, surname, instagram, rating, city, email, password, post;

    TextView fullname, instaLogin, userRating, emailTV, locationTV, money_amount, position;

    RelativeLayout layoutInstagram;

    Button paymentImage;
    ImageView insta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        name = sharedPreferences.getString("name", "anon");
        surname = sharedPreferences.getString("surname", "anon");
        rating = sharedPreferences.getString("rating", "0");
        city = sharedPreferences.getString("city", "default");
        instagram = sharedPreferences.getString("instagramLogin", "anon");
        email = sharedPreferences.getString("email", "anon");
        new UserData().execute();
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        paymentImage = (Button) getActivity().findViewById(R.id.cashOut);
        insta=(ImageView)getActivity().findViewById(R.id.imageInstagram);
        fullname = (TextView) getActivity().findViewById(R.id.userNameTV);
        instaLogin = (TextView) getActivity().findViewById(R.id.textView);
        userRating = (TextView) getActivity().findViewById(R.id.moneyForPost);
        emailTV = (TextView) getActivity().findViewById(R.id.emailTV);
        locationTV = (TextView) getActivity().findViewById(R.id.locationTV);
        money_amount = (TextView) getActivity().findViewById(R.id.money_amount);
        position=(TextView)getActivity().findViewById(R.id.position);

        layoutInstagram = (RelativeLayout) getActivity().findViewById(R.id.relativeLayout2);

        layoutInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RatingHistory.class);
                startActivity(intent);
            }
        });

        paymentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }
        });

//        userPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        name = userPrefs.getString("name", null);
//        surname = userPrefs.getString("surname", null);
//        instagram = userPrefs.getString("instagramLogin", null);
//        rating = userPrefs.getString("rating", null);


        fullname.setText(name + " " + surname);
        instaLogin.setText(instagram);
        userRating.setText(rating);
        emailTV.setText(email);
        locationTV.setText(city);

    }

    public class UserData extends AsyncTask {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String categories = null;
        String avatar = null;

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", sp.getString("password", "null")));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/show", "POST", nameValuePairs);

            Log.d("USER", jsonObject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (jsonObject.has("success")) {

                try {
                    String name = jsonObject.getString("name");
                    String surname = jsonObject.getString("surname");
                    String money = jsonObject.getString("money");
                    String city = jsonObject.getString("city");

                    JSONArray jsonArray = jsonObject.getJSONArray("social");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject socialObject = jsonArray.getJSONObject(i);
                        instagram = socialObject.getString("login");
                        rating = socialObject.getString("rating");
                        categories = socialObject.getString("categories");
                        avatar = socialObject.getString("avatar");
                        avatar = avatar.replace("\\/", "/");
                        post=socialObject.getString("place");
                    }

                    fullname.setText(name + " " + surname);
                    instaLogin.setText(instagram);
                    userRating.setText(rating);
                    emailTV.setText(email);
                    locationTV.setText(city);
                    money_amount.setText(money);
                    position.setText(post);
                    //ImageLoader imageLoader=new ImageLoader(getActivity());
                   // imageLoader.DisplayImage(avatar,insta);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void openRating(View v) {
        Intent intent = new Intent(getActivity(), RatingHistory.class);
        startActivity(intent);
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