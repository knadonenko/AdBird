package test.com.adbirdads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;


public class CategoriesUpdate extends Activity implements OnTaskCompleted {

    public Map<String, String> themeIdAndName = new HashMap<String, String>();
    public ThemesTask themeTask = null;
    public ArrayList<CheckBox> cb = new ArrayList<CheckBox>();

    int[] checkBoxId = new int[]{0, R.id.th_1, R.id.th_2, R.id.th_3, R.id.th_4, R.id.th_5, R.id.th_6,
            R.id.th_7, R.id.th_8, R.id.th_9, R.id.th_10, R.id.th_11, R.id.th_12, R.id.th_13, R.id.th_14, R.id.th_15};

    ArrayList<Integer> usualCat = new ArrayList<Integer>();
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14, cb15;
    public Iterator<CheckBox> itr;
    public int cbChecked1 = 4;
    public int cbChecked2 = 6;
    public int cbChecked3 = 5;

    public ArrayList<Integer> themeIds;
    public HashMap<String, String> themes;
    public Button selectedBtn = null;
    public String language = "";
    public ToggleButtonGroupTableLayout tableLayout = null;
    public TextView title = null;
    int firstChecked, secondChecked;
    int numberOfCheckboxesChecked;
    CheckBox temp;
    Button nextButton;

    int checkBox_num;

    String usualCategories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_update);

        ActionBar action_bar = getActionBar();
        action_bar.setDisplayHomeAsUpEnabled(true);
        action_bar.setTitle(getString(R.string.change_category));

        //action_bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        //nextButton = (Button) findViewById(R.id.next_button);

        SharedPreferences myPr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String myPref_text = myPr.getString("locale", null);

        	language = getString(R.string.locale);


        title = (TextView) this.findViewById(R.id.textView1);
        tableLayout = (ToggleButtonGroupTableLayout) this.findViewById(R.id.radGroup1);
        selectedBtn = (Button) this.findViewById(R.id.button);


        if (isNetworkAvailable()) {
            new getCategories().execute();
            themeTask = new ThemesTask(this);
            themeTask.setContext(getApplicationContext());
            themeTask.execute((Void) null);


        } else {

            Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        cb1 = (CheckBox) findViewById(R.id.th_1);
        cb2 = (CheckBox) findViewById(R.id.th_2);
        cb3 = (CheckBox) findViewById(R.id.th_3);
        cb4 = (CheckBox) findViewById(R.id.th_4);
        cb5 = (CheckBox) findViewById(R.id.th_5);
        cb6 = (CheckBox) findViewById(R.id.th_6);
        cb7 = (CheckBox) findViewById(R.id.th_7);
        cb8 = (CheckBox) findViewById(R.id.th_8);
        cb9 = (CheckBox) findViewById(R.id.th_9);
        cb10 = (CheckBox) findViewById(R.id.th_10);
        cb11 = (CheckBox) findViewById(R.id.th_11);
        cb12 = (CheckBox) findViewById(R.id.th_12);
        cb13 = (CheckBox) findViewById(R.id.th_13);
        cb14 = (CheckBox) findViewById(R.id.th_14);
        cb15 = (CheckBox) findViewById(R.id.th_15);



        if (cb1.isChecked()) {
            usualCategories += "1";
        }
        if (cb2.isChecked()) {
            usualCategories += "2";
        }
        if (cb3.isChecked()) {
            usualCategories += "3";
        }
        if (cb4.isChecked()) {
            usualCategories += "4";
        }
        if (cb5.isChecked()) {
            usualCategories += "5";
        }
        if (cb6.isChecked()) {
            usualCategories += "6";
        }
        if (cb7.isChecked()) {
            usualCategories += "7";
        }
        if (cb8.isChecked()) {
            usualCategories += "8";
        }
        if (cb9.isChecked()) {
            usualCategories += "9";
        }
        if (cb10.isChecked()) {
            usualCategories += "10";
        }
        if (cb11.isChecked()) {
            usualCategories += "11";
        }
        if (cb12.isChecked()) {
            usualCategories += "12";
        }
        if (cb13.isChecked()) {
            usualCategories += "13";
        }
        if (cb14.isChecked()) {
            usualCategories += "14";
        }
        if (cb15.isChecked()) {
            usualCategories += "15";
        }

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


        itr = cb.iterator();


        selectedBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new setCategories().execute();
            }
        });



		/*if(language.equals("ru")){
            action_bar.setTitle(getResources().getString(R.string.categories));
			title.setText(getResources().getString(R.string.title_categories));
			selectedBtn.setImageResource(R.drawable.next_button_ru);
		}else{
			action_bar.setTitle(getResources().getString(R.string.categories));
			title.setText(getResources().getString(R.string.title_categories));
			selectedBtn.setImageResource(R.drawable.next_button);
		}*/


    }

    class setCategories extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String url = "http://api.adbird.net/index.php/surfer/SetCategories";
        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(CategoriesUpdate.this);

        @Override
        protected String doInBackground(String... args0) {

            String email = userPreferences.getString("email", "anon");
            String password = userPreferences.getString("password", "anon");
            String stId = userPreferences.getString("socialID", "1");
            String login = userPreferences.getString("instagramLogin", "anon");
            if (usualCat.size() > 1) {
                usualCategories = String.valueOf(usualCat.get(usualCat.size() - 1) + ", " + usualCat.get(usualCat.size() - 2)+ ", " + usualCat.get(usualCat.size() - 3));
            } else {
                usualCategories = String.valueOf(usualCat);
            }
            usualCategories="";
            int i = 1;
            for (CheckBox box : cb) {
                if (box.isChecked()) {
                    usualCategories+=""+i+",";

                }
                i++;
            }
            usualCategories=usualCategories.substring(0, usualCategories.length()-1);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("stId", stId));
            params.add(new BasicNameValuePair("login", login));
            params.add(new BasicNameValuePair("usualCategories", usualCategories));


            jsonObject = jsonParser.makeHttpRequest(url, "POST", params);
            Log.d("JSON", jsonObject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (jsonObject.has("success")) {
                Toast.makeText(getBaseContext(), getString(R.string.categories_updated), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }else if(jsonObject.has("error")){
                Toast.makeText(getBaseContext(), getString(R.string.categories_updated_deny), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class getCategories extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        JSONArray jsonArray, jsonArray1;
        String url = "http://api.adbird.kz/index.php/surfer/getCategories";
        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(CategoriesUpdate.this);

        @Override
        protected String doInBackground(String... args0) {

            String email = userPreferences.getString("email", "anon");
            String password = userPreferences.getString("password", "anon");
            String stId = userPreferences.getString("socialID", "1");
            String login = userPreferences.getString("instagramLogin", "anon");


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("stId", stId));
            params.add(new BasicNameValuePair("login", login));


            jsonArray = jsonParser.makeArrayRequest(url, "POST", params);
//            Log.d("json str", jsonArray.toString());
//            Log.d(email, password);
//            Log.d(stId, login);
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                jsonArray1 = jsonArray.getJSONArray(0);
                jsonObject = jsonArray1.getJSONObject(0);
                cbChecked1 = Integer.parseInt(jsonObject.getString("id")) - 1;
                // firstChecked=cbChecked1;
                jsonArray1 = jsonArray.getJSONArray(1);
                jsonObject = jsonArray1.getJSONObject(0);
                cbChecked2 = Integer.parseInt(jsonObject.getString("id")) - 1;
                //secondChecked=cbChecked2;
                jsonArray1 = jsonArray.getJSONArray(2);
                jsonObject = jsonArray1.getJSONObject(0);
                cbChecked3 = Integer.parseInt(jsonObject.getString("id")) - 1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class UserData extends AsyncTask {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        String categories = null;
        String avatar = null;

        String instLogin;
        String name;
        String surname;
        String rating = null;

        SharedPreferences userPreferences = PreferenceManager.getDefaultSharedPreferences(CategoriesUpdate.this);

        @Override
        protected Object doInBackground(Object[] objects) {

            String email = userPreferences.getString("email", "anon");
            String password = userPreferences.getString("password", "anon");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("password", password));

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
                        instLogin = socialObject.getString("login");
                        rating = socialObject.getString("rating");
                        categories = socialObject.getString("categories");
                        avatar = socialObject.getString("avatar");
                        avatar = avatar.replace("\\/", "/");
                    }

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(CategoriesUpdate.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("surname", surname);
                    editor.putString("money", money);
                    editor.putString("city", city);
                    editor.putString("instagramLogin", instLogin);
                    editor.putString("rating", rating);
                    editor.putString("categories", categories);
                    editor.putString("avatar", avatar);
                    editor.commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(CategoriesUpdate.this, MainActivity.class);
                startActivity(intent);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ThemesTask extends AsyncTask<Void, Void, Boolean> {
        private OnTaskCompleted listener;
        private String themeId;
        private String themeName;
        private ContentResolver resolver;
        private Context context;
        private ProgressDialog dialog = new ProgressDialog(CategoriesUpdate.this);

        public ThemesTask(OnTaskCompleted listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            tableLayout.setVisibility(View.GONE);
            //selectedBtn.setVisibility(View.GONE);
            String msg =getString(R.string.please_wait);

            this.dialog.setMessage(msg);
            this.dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Boolean success = false;
            try {
                getThemes();
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

        public void setContext(Context newContext) {
            this.context = newContext;
        }

        public Boolean getThemes() throws UnsupportedEncodingException,
                JSONException {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://api.adbird.net/index.php/server/getCategories");
            // Execute HTTP Post Request

            resolver = context.getContentResolver();

            try {
                HttpResponse response = httpclient.execute(httppost);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String json = reader.readLine();

                JSONArray jsonArray = new JSONArray(json);
                ContentValues values = new ContentValues();
                // Delete the old themes
                resolver.delete(AdBirdContentProvider.CONTENT_URI_THEMES, null,
                        null);
                for (int i = 0; i < jsonArray.length(); i++) {
                    if ((jsonArray.getJSONObject(i)).has("id")) {
                        String id = (jsonArray.getJSONObject(i)).get("id")
                                .toString();
                        String name = (jsonArray.getJSONObject(i)).get("name")
                                .toString();
                        values.put(DatabaseHandler.KEY_THEMES_NAME, name);
                        String name_rus = (jsonArray.getJSONObject(i)).get(
                                "name_rus").toString();
                        values.put(DatabaseHandler.KEY_THEMES_NAME_RUS,
                                name_rus);
                        values.put(DatabaseHandler.KEY_ID, id);

                        try {
                            resolver.insert(
                                    AdBirdContentProvider.CONTENT_URI_THEMES,
                                    values);
                        } catch (SQLiteConstraintException e) {
                            Log.d("login", "already exists " + e);
                        }
                        Log.d("theme", "insert into THEMES is successful");

                    }
                }

            } catch (IOException e) {
                Log.d("theme", "error execute");
                Log.d("theme", e.toString());
            }
            Log.d("theme", "response execute");
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            tableLayout.setVisibility(View.VISIBLE);


            // GET THEMES FROM DATABASE
            String themeLang = "";
            if (language.equals("en")) {
                themeLang = DatabaseHandler.KEY_THEMES_NAME;
            } else {
                themeLang = DatabaseHandler.KEY_THEMES_NAME_RUS;
            }
            Cursor themeCursor = resolver.query(AdBirdContentProvider.CONTENT_URI_THEMES, new String[]{DatabaseHandler.KEY_ID, themeLang}, null, null, null);
            while (themeCursor.moveToNext()) {
                int themeIdIndex = themeCursor.getColumnIndex(DatabaseHandler.KEY_ID);
                int themeNameIndex = themeCursor.getColumnIndex(themeLang);
                themeId = themeCursor.getString(themeIdIndex);
                themeName = themeCursor.getString(themeNameIndex);
                themeIdAndName.put(themeName, themeId);
            }
            listener.onTaskCompleted();

        }

        @Override
        protected void onCancelled() {

        }

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onTaskCompleted() {
        @SuppressWarnings("rawtypes")
        Iterator countit = themeIdAndName.entrySet().iterator();

        this.themeIds = new ArrayList<Integer>();
        this.themes = new HashMap<String, String>();
        while (countit.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry pairs = (Map.Entry) countit.next();
            int id = Integer.parseInt(pairs.getValue().toString());
            this.themes.put(pairs.getValue().toString(), pairs.getKey()
                    .toString());
            themeIds.add(id);
            countit.remove();
        }
        Collections.sort(themeIds);
        int index = 0;
        for (CheckBox box : cb) {
            String themeId = themeIds.get(index).toString();
            box.setText(this.themes.get(themeId));
            index++;
        }
        cb.get(cbChecked1).setChecked(true);
        cb.get(cbChecked2).setChecked(true);
        cb.get(cbChecked3).setChecked(true);
        //cbChecked1=3;
        //cbChecked2=8;
    }

    public void onCheckboxClicked(View view) {


        checkBoxCount(view);
    }

    public void checkBoxCount(View v) {


        String max_themes = "";
        String min_theme = "";
        if (language.equals("ru")) {
            max_themes = getString(R.string.max_themes);
            min_theme = getString(R.string.min_theme);
        } else {
            max_themes = getString(R.string.max_themes);
            min_theme = getString(R.string.min_theme);
        }
        int numChecked = 3;
        int boxIndex = 0;
        int i = 0;
        for (CheckBox box : cb) {
            if (box.isChecked()) {
                numChecked++;
            }
            if (box == (CheckBox) v) {
                boxIndex = i;
                usualCat.add(boxIndex + 1);
            }
            i++;
        }
        if (numChecked == 0) {
            numChecked++;
            //Toast.makeText(getBaseContext(), min_theme, Toast.LENGTH_SHORT)
            //  .show();
        }
        if (numChecked == 1) {
            if (boxIndex != this.cbChecked2) {
                this.cbChecked1 = boxIndex;
            }
            selectedBtn.setEnabled(true);
            ((CheckBox) v).setChecked(true);
        }
        if (numChecked == 2) {
            if (boxIndex != this.cbChecked1) {
                this.cbChecked2 = boxIndex;
            }

            ((CheckBox) v).setChecked(true);
        }

        if (numChecked > 3) {
            // Toast.makeText(getBaseContext(), max_themes, Toast.LENGTH_SHORT)
            // .show();
            int j = 0;
            for (CheckBox box : cb) {
                if (j == this.cbChecked1) {
                    box.setChecked(false);

                    //usualCat.remove(j);
                }
                j++;
            }
            if (boxIndex != this.cbChecked1 && boxIndex != this.cbChecked2 &&boxIndex != this.cbChecked3) {
                this.cbChecked1 = this.cbChecked2;
                this.cbChecked2 = this.cbChecked3;
                this.cbChecked3=boxIndex;
            }
            ((CheckBox) v).setChecked(true);
        }
    }

    public void showCat(View v) {
        usualCategories = String.valueOf(usualCat.get(usualCat.size() - 1) + ", " + usualCat.get(usualCat.size() - 2));
        Log.d("CATEGORIES", String.valueOf(usualCat));
        Log.d("category", usualCategories);
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
        usualCategories = String.valueOf(checkedThemes);
        return checkedThemes;
    }


    public void showNoNetwork(){
        Toast.makeText(getBaseContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}