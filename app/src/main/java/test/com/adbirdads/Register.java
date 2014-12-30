package test.com.adbirdads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationClient;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import test.com.adbirdads.yandexlocator.LbsInfo;
import test.com.adbirdads.yandexlocator.LbsLocationListener;
import test.com.adbirdads.yandexlocator.WifiAndCellCollector;


public class Register extends Activity implements LbsLocationListener {

    //UI
    private EditText name, surname, email, password, conf_password, dd, mm, yyyy;
    private RadioButton male, female;
    private RadioGroup radioGroupReg;
    private AlertDialog alert;
    private Button register_button;
    private ProgressDialog progressDialog;
    TextView city, country;
    Button buttonDate;

    Boolean firstTime;

    private static final String TAG_RESULTS = "results";

    JSONArray results = null;

    private Register instance;
    private WifiAndCellCollector wifiAndCellCollector;

    //String values from editText
    private String mEmail, mPassword, mName, mSurname, dateText;

    LocationClient mLocationClient;

    private TextView addressLabel;
    private TextView locationLabel;
    private TextView haveAccount;

    public String counName;
    public String getCityName;
    public String intentCityName = "";
    String lat, lng;
    private ArrayList<String> countryName = new ArrayList<String>();
    private Map<String, String> countryCCFips = new HashMap<String, String>();
    private ArrayList<String> cityName = new ArrayList<String>();
    private Map<String, String> cityAndId = new HashMap<String, String>();

    Calendar c = Calendar.getInstance();
    int startYear = c.get(Calendar.YEAR);
    int startMonth = c.get(Calendar.MONTH);
    int startDay = c.get(Calendar.DAY_OF_MONTH);
    int DIALOG_DATE = 1;

    Geocoder gcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        firstTime = sp.getBoolean("firstTime", true);

        if (!firstTime) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        haveAccount = (TextView) findViewById(R.id.haveAccount);
        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        Boolean firstTimeHere = false;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editorFirst = sharedPreferences.edit();
        editor.putBoolean("firstTime", firstTimeHere);
        editor.commit();

        gcd = new Geocoder(Register.this, Locale.ENGLISH);
        //city = (TextView) findViewById(R.id.getLocation);
        //country = (TextView) findViewById(R.id.addressLabel);

        buttonDate = (Button) findViewById(R.id.buttonDate);

        instance = this;
        SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
        String uuid = settings.getString("UUID", null);
        if (uuid == null) {
            uuid = generateUUID();
            SharedPreferences.Editor edit = settings.edit();
            edit.putString("UUID", uuid);
            edit.commit();
        }
        wifiAndCellCollector = new WifiAndCellCollector(this, this, uuid);

//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.hide();
//        }
//        progressDialog = ProgressDialog.show(instance, null, "Please wait");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifiAndCellCollector.requestMyLocation();
            }
        }).start();



        //locationLabel = (TextView) findViewById(R.id.getLocation);
        //addressLabel = (TextView) findViewById(R.id.addressLabel);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        //conf_password = (EditText) findViewById(R.id.conf_password);
        //dd = (EditText) findViewById(R.id.dd);
        //mm = (EditText) findViewById(R.id.mm);
        //yyyy = (EditText) findViewById(R.id.yyyy);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        radioGroupReg = (RadioGroup) findViewById(R.id.radioGroupReg);

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });

/*        dd.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mDay = dd.getText().toString();
                if(!mDay.isEmpty()){
                    int day = Integer.parseInt(mDay);
                    if(day>31){
                            dd.setError(getString(R.string.invalidDay));
                    }
                }
            }
        });

        mm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mMonth = mm.getText().toString();
                if(!mMonth.isEmpty()){
                    int month = Integer.parseInt(mMonth);
                    if(month>12){
                            mm.setError(getString(R.string.invalidMonth));
                    }
                }
            }
        });

        yyyy.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mYear = yyyy.getText().toString();
                if(!mYear.isEmpty()){
                    int year = Integer.parseInt(mYear);
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    if(year>currentYear){
                            yyyy.setError(getString(R.string.invalidYear));
                    }
                }
            }
        });*/

/*        conf_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mPassword = password.getText().toString();
                    mConfPassword = conf_password.getText().toString();

                    if (!mPassword.equals(mConfPassword)) {
                        String error = getString(R.string.passwords_dont_match);
                        conf_password.setError(error);
                    }
                }
            }
        });*/

        register_button = (Button) findViewById(R.id.register_button);

        male.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(male.getWindowToken(), 0);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        wifiAndCellCollector.startCollect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wifiAndCellCollector.stopCollect();
    }

    //Check Location with help of Yandex Locator
    @Override
    public void onLocationChange(final LbsInfo lbsInfo) {
        if(isNetworkAvailable()) {
            if (lbsInfo != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.hide();
                        }
                        if (lbsInfo.isError) {
                            if (alert != null && alert.isShowing()) {
                                alert.hide();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(instance);
                            builder.setMessage(lbsInfo.errorMessage)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            alert = builder.create();
                            alert.show();
                            //lbsLatitude.setText("");
                            //lbsLongtitude.setText("");
                            //lbsAltitude.setText("");
                            //lbsPrecision.setText("");
                            //lbsType.setText("");
                        } else {
                            lat = lbsInfo.lbsLatitude;
                            lng = lbsInfo.lbsLongtitude;
                            //Log.d("COORDINATES", lbsInfo.lbsLatitude);
                            //Log.d("COORDINATES", lbsInfo.lbsLongtitude);

                            if (lat != null && lng != null) {

                                try {
                                    new GetGeo().execute().get(10000, TimeUnit.MILLISECONDS);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                }
                            }
                            //lbsLatitude.setText("Latitude="+lbsInfo.lbsLatitude);
                            //lbsLongtitude.setText("Longtitude="+lbsInfo.lbsLongtitude);
                            //lbsAltitude.setText("Altitude="+lbsInfo.lbsAltitude);
                            //lbsPrecision.setText("Precision="+lbsInfo.lbsPrecision);
                            //lbsType.setText("Type="+lbsInfo.lbsType);
                        }
                    }
                });
            }
        }
    }

    private class GetGeo extends AsyncTask<String, String, String> {

        String City, Country;
        String Status;
        JSONObject zero2;
        JSONObject zero;
        JSONArray address_components;

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser = new JSONParser();

            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                    + lat + "," + lng + "&sensor=true";

            JSONObject json = jsonParser.getJSONFromUrl(url);
            Log.d("URL", url);
            Log.d("ADDRESS", json.toString());

            try {
                Status = json.getString("status");
                if(Status.equals("OK")) {
                    results = json.getJSONArray(TAG_RESULTS);
                    zero = results.getJSONObject(0);
                    address_components = zero.getJSONArray("address_components");


                    for(int i = 0; i< address_components.length(); i++) {
                        zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                            if (Type.equalsIgnoreCase("street_number")) {
                                String Address1 = long_name + " ";
                            } else if (Type.equalsIgnoreCase("route")) {
                                String Address1 = "";
                                Address1 = Address1 + long_name;
                            } else if (Type.equalsIgnoreCase("sublocality")) {
                                String Address2 = long_name;
                            } else if (Type.equalsIgnoreCase("locality")) {
                                // Address2 = Address2 + long_name + ", ";
                                City = long_name;
                                getCityName = City;
                                Log.d("City", City);
                            } else if (Type.equalsIgnoreCase("country")) {
                                Country = long_name;
                                counName = Country;
                                Log.d("Country", Country);
                            } else if (Type.equalsIgnoreCase("postal_code")) {
                                String PIN = long_name;
                            }
                        }
                    }

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(Status.equals("OK")) {
//                city.setText(City);
//                country.setText(Country);
            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    private boolean checkEmptyField() {
        mName = name.getText().toString();
        mSurname = surname.getText().toString();
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();

//        mConfPassword = conf_password.getText().toString();
//        mDay= dd.getText().toString();
//        mMonth = mm.getText().toString();
//       mYear = yyyy.getText().toString();
        if (mName.trim().equals("") || mSurname.trim().equals("") || mEmail.trim().equals("") ||
                mPassword.trim().equals("") ) {
            String ok = getString(R.string.okBtn);
            return false;
        }else

         if (dateText == null || dateText.equals(" ")  || dateText.equals("")) {
            return false;
        }
        else if(!mEmail.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            email.setError(getString(R.string.invalidEmail));
            return false;
        } else {
            return true;
        }

    }

    public void choseDate(View v) {
        showDialog(DIALOG_DATE);

     }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, startYear, startMonth, startDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            startYear = year;
            startMonth = monthOfYear + 1;
            startDay = dayOfMonth;
            dateText = startYear + "-" + startMonth + "-" + startDay;
            buttonDate.setText(startYear + "/" + startMonth + "/" + startDay);
            email.requestFocus();
            Log.d("DATE IS ", dateText);
        }
    };




    public void registerUser(View v) {
        boolean fieldsOk=true;
        checkEmptyField();

        if(name.getText().toString().trim().equals("")){


            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalidName),
                    Toast.LENGTH_SHORT).show();
            fieldsOk=false;

        }else
        if(surname.getText().toString().trim().equals("")){


            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalidSurname),
                    Toast.LENGTH_SHORT).show();
            fieldsOk=false;

        }else

        if (dateText == null || dateText.equals(" ")  || dateText.equals("")) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalidDay),
                    Toast.LENGTH_SHORT).show();
            fieldsOk=false;
        }else
        if(email.getText().toString().trim().equals("")){

            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalidEmail),
                    Toast.LENGTH_SHORT).show();
            fieldsOk=false;

        }else
        if(!mEmail.contains("@") || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){

            Toast.makeText(getBaseContext(), getResources().getString(R.string.invalidEmail),
                    Toast.LENGTH_SHORT).show();
            fieldsOk=false;
        }else
        if(mPassword.length()<6) {

            Toast.makeText(getBaseContext(), getResources().getString(R.string.error_invalid_password),
                    Toast.LENGTH_SHORT).show();
            fieldsOk=false;
        }
        if(fieldsOk){
            new CheckMailTask().execute();
        }
    }

    class CheckMailTask extends AsyncTask {

        ProgressDialog pDialog;

        JSONParser jsonParser = new JSONParser();
        JSONObject json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email.getText().toString().trim()));

            json = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/checkEmail", "POST", params);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pDialog.dismiss();

            if(json.has("success")) {
                Intent intent = new Intent(Register.this,PublicOffer.class);
                intent.putExtra("name", name.getText().toString().trim());
                intent.putExtra("surname", surname.getText().toString().trim());
                intent.putExtra("email", email.getText().toString().trim());
                intent.putExtra("password", password.getText().toString().trim());
                intent.putExtra("sex",getRadioButtonValue()+"");
                intent.putExtra("birthday", dateText);
                intent.putExtra("country", counName);
                intent.putExtra("city",getCityName );

                if(checkEmptyField()) {

                    startActivity(intent);

                } else {
                    //Log.d("SASAI", "SASAI");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Register.this);
                    builder1.setMessage(getString(R.string.check_your_credentials));
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                }
                SharedPreferences sp = PreferenceManager
                        .getDefaultSharedPreferences(Register.this);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("email", email.getText().toString().trim());
                edit.putString("password", password.getText().toString().trim());
                edit.putString("name",name.getText().toString().trim());
                edit.putString("surname",surname.getText().toString().trim());
                edit.putString("avatar",null);
                edit.commit();
            } else if(json.has("error")) {

                Toast.makeText(Register.this, getResources().getString(R.string.emailError), Toast.LENGTH_LONG).show();

            }

        }
    }

    public int getRadioButtonValue() {
        int radioGroupId = radioGroupReg.getCheckedRadioButtonId();
        if (radioGroupId == -1) {

        }
        View gender = radioGroupReg.findViewById(radioGroupId);
        int selectedSexIndex = radioGroupReg.indexOfChild(gender);
        return selectedSexIndex;
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                Register.this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        Register.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }



    /*class GetCurrentLocation extends AsyncTask<Void,Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            Geocoder geocoder = new Geocoder(Register.this, Locale.ENGLISH);
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(gps.getLatitude(),gps.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                String errorString = "Illegal arguments " +Double.toString(43.220413) +" , " +Double.toString(76.924307) +" passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                //String addressText = String.format("%s, %s, %s",address.getMaxAddressLineIndex() > 0 ?address.getAddressLine(0) : "",address.getLocality(),address.getCountryName());
                String addressText = String.format("%s",address.getMaxAddressLineIndex() > 0 ?address.getLocality():"");
                return addressText;
            } else {
                return "No address found";
            }
        }
        @Override
        protected void onPostExecute(final String success) {
            getCityName = success;
            //Toast.makeText(getApplicationContext(), getCityName, Toast.LENGTH_LONG).show();
        }
    }

    public class GetCountriesTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog = new ProgressDialog(Register.this);

        @Override
        protected void onPreExecute() {
            String msg = "";
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage(Register.this.getString(R.string.pleaseWait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean success = false;
            try {
                getCountries();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        public Boolean getCountries() throws UnsupportedEncodingException,JSONException {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.adbird.net/index.php/site/getCountries");
            try {
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if ((jsonArray.getJSONObject(i)).has("country_name")) {
                        countryName.add(((jsonArray.getJSONObject(i)).get("country_name")).toString());
                        countryCCFips.put(((jsonArray.getJSONObject(i)).get("country_name")).toString(), ((jsonArray.getJSONObject(i)).get("cc_fips")).toString());
                    }
                }
            } catch (IOException e) {
                Log.d("login", "error execute");
                Log.d("login", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            for(int i=0;i<countryName.size();i++){
                if(countryName.get(i).equals("Kazakhstan")){
                    counName = countryCCFips.get(countryName.get(i));
                }
            }
            //Toast.makeText(getApplicationContext(), counName, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
        }
    }//End Get Contries

    // Start Get Cities
    public class GetCitiesTask extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog = new ProgressDialog(Register.this);

        @Override
        protected void onPreExecute() {
            String msg = "";
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage(Register.this.getString(R.string.pleaseWait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean success = false;
            try {
                getCities();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        public Boolean getCities() throws UnsupportedEncodingException,JSONException {
            String d = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.adbird.net/index.php/site/getCities");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("cc_fips", counName));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            try {
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if ((jsonArray.getJSONObject(i)).has("full_name_nd")) {
                        cityName.add(((jsonArray.getJSONObject(i)).get("full_name_nd")).toString());
                        cityAndId.put(((jsonArray.getJSONObject(i)).get("full_name_nd")).toString(), ((jsonArray.getJSONObject(i)).get("id")).toString());
                    }
                }
            } catch (IOException e) {
                Log.d("login", "error execute");
                Log.d("login", e.toString());
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            String test = "";
            for(int i=0;i<cityName.size();i++){
                if(cityName.get(i).toLowerCase().equals("shymkent1118385")){
                    test = cityName.get(i);
                    Log.d("CITY", test);
                }
            }
            //Toast.makeText(getApplicationContext(), cityName.get(0)+test+cityName.size(), Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {

        }
    }//End Get Cities*/

    public String generateUUID() {
        UUID uuid = UUID.randomUUID();
        StringBuilder str = new StringBuilder(uuid.toString());
        int index = str.indexOf("-");
        while (index > 0) {
            str.deleteCharAt(index);
            index = str.indexOf("-");
        }
        return str.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showNoNetwork(){
        Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}