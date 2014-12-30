package test.com.adbirdads;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Forgot_Password extends Activity{

    //UI references
    EditText emailEditText;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        emailEditText = (EditText) findViewById(R.id.emailEditText);

    }

    public void SendEmail(View v) {
        if(!emailEditText.getText().toString().trim().equals("")){
            new SendEmail().execute();
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.enterEmail), Toast.LENGTH_LONG).show();
        }
    }

    private class SendEmail extends AsyncTask<String, String, String> {

        Boolean success = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Forgot_Password.this);
            pDialog.setMessage(Forgot_Password.this.getString(R.string.pleaseWait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser = new JSONParser();
            JSONObject json;

            String email = emailEditText.getText().toString();
            String forgetMail = "http://api.adbird.net/index.php/surfer/remindPassword";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));

            json = jsonParser.makeHttpRequest(forgetMail, "POST", params);

            try {

                if (json.equals(null)) {
                    return null;
                } else if (json.has("success")) {
                    //Toast.makeText(getApplicationContext(), "Password reset was successful", Toast.LENGTH_LONG).show();
                    success = true;
                } else if (json.has("error") && json.getString("error").equals("email invalid")) {
                    Log.d("login", "email wrong " + json);
                    success = false;
                    //Toast.makeText(getApplicationContext(), "Invalid email please try again", Toast.LENGTH_LONG).show();
                } else if (json.has("error") && json.getString("error").equals("email invalid")) {
                    success = false;
                    //Toast.makeText(getApplicationContext(), "Invalid email please try again", Toast.LENGTH_LONG).show();
                } else {
                    success = false;
                    Log.d("login", "fail login " + json);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

            if (success) {
                Toast.makeText(getApplicationContext(), "Password reset was successful", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid email please try again", Toast.LENGTH_LONG).show();
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
