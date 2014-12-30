package test.com.adbirdads;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChangePassword extends Activity {

    EditText oldPass, newPass, confPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.changePassTxt));

        oldPass = (EditText) findViewById(R.id.oldPass);
        newPass = (EditText) findViewById(R.id.newPass);
        confPass = (EditText) findViewById(R.id.confirmPass);
    }

    public void newPassword(View v) {
        if(checkFields() == true) {
            new ChangePasswordTask().execute();
        } else {
            Toast.makeText(ChangePassword.this, getResources().getString(R.string.passwordMismatch), Toast.LENGTH_LONG).show();
        }

    }

    public boolean checkFields() {
        String newPassword = newPass.getText().toString();
        String confPassword = newPass.getText().toString();

        if (newPassword.equals(confPassword)) {
            return true;
        } else {
            return false;
        }
    }

    class ChangePasswordTask extends AsyncTask {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        @Override
        protected Object doInBackground(Object[] objects) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
            String email = sp.getString("email", null);
            String password = oldPass.getText().toString();
            String newPassword = newPass.getText().toString();
            String confPassword = confPass.getText().toString();

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("newPassword", newPassword));
            params.add(new BasicNameValuePair("newPassword1", confPassword));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/changePassword", "POST", params);
            Log.d("JSON", jsonObject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(jsonObject.has("success")) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", newPass.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.password_changed), Toast.LENGTH_LONG).show();
                finish();
            } else if (jsonObject.has("error")) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.saving_err), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
