package test.com.adbirdads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SocialNetworksConfig extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialnetworks_config);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.title_activity_social_networks_config);
    }

    public void openCategories(View v) {
        Intent intent = new Intent(SocialNetworksConfig.this, CategoriesUpdate.class);
        startActivity(intent);
    }

    public void deactivateSocial(View v) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(SocialNetworksConfig.this);
        builder.setMessage(getString(R.string.sure)).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DeactivateSocial().execute();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();


    }


    public class DeactivateSocial  extends AsyncTask {
        SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(SocialNetworksConfig.this);
        ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SocialNetworksConfig.this);
            pDialog.setMessage(getResources().getString(R.string.pleaseWait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String email = userPrefs.getString("email", null);
            String password = userPrefs.getString("password", null);
            String instagram = userPrefs.getString("instagramLogin", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("login", instagram));
            params.add(new BasicNameValuePair("stId", "1"));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/deactivateSocial", "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pDialog.dismiss();
            if (jsonObject.has("success")) {
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.remove("instagramLogin");
                editor.remove("email");
                editor.remove("password");
                editor.remove("socialized");
                editor.commit();
                finish();
                Toast.makeText(SocialNetworksConfig.this, getResources().getString(R.string.accountOff), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SocialNetworksConfig.this, Login.class);
                startActivity(intent);
            }
        }
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
}



