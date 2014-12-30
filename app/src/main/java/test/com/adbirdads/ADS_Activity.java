package test.com.adbirdads;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;


public class ADS_Activity extends Activity {

    ImageView posterIMG;
    ImageLoader imageLoader = new ImageLoader(this);

    TextView titleText, dateText, textDescription;
    String poster, title, brand, datetxt, post_id, amount;
    Button postAd;
    ProgressDialog progressDialog;
    boolean ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.title_activity_ads));

        posterIMG = (ImageView) findViewById(R.id.adsImage);
        titleText = (TextView) findViewById(R.id.titleText);
        dateText = (TextView) findViewById(R.id.dateText);
        textDescription = (TextView) findViewById(R.id.textDescription);
        RelativeLayout warn=(RelativeLayout)findViewById(R.id.relativeLayout6);
        ImageView quest=(ImageView)findViewById(R.id.imageQuestion);
        TextView textMoney=(TextView)findViewById(R.id.textMoney);
        Intent intent = getIntent();
        poster = intent.getStringExtra("poster");
        title = intent.getStringExtra("title");
        brand = intent.getStringExtra("brand");
        datetxt = intent.getStringExtra("date");
        post_id = intent.getStringExtra("id");
        amount=intent.getStringExtra("amount");
        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        String rating=sp.getString("rating", "0");

        datetxt = datetxt.replace("-", ".");
        String[] parts = new String[2];
        parts = datetxt.split(" ");

        datetxt = parts[0];

        titleText.setText(brand);
        dateText.setText(datetxt);
        textDescription.setText(title);


        imageLoader.DisplayImage(poster, posterIMG);
        postAd = (Button) findViewById(R.id.postButton);
        //double offeredMoney=Double.parseDouble(amount);
        int ratingUser=Integer.parseInt(rating);
        if(amount.indexOf(".")>0){//if amount is not exact number
        amount=amount.substring(0,amount.indexOf("."));
        }
        Log.d("offered money", amount);
        int moneyToShow= Integer.parseInt(amount);
        if(ratingUser>moneyToShow){
            warn.setVisibility(View.VISIBLE);
            textMoney.setText(moneyToShow+getResources().getString(R.string.tg));
        }

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ADS_Activity.this, FaqActivity.class);
                i.putExtra("aboutmoney", true);
                startActivity(i);
            }
        });
        new checkTime().execute();

    }

    public void makePost(View v) {

        final AlertDialog.Builder builder=new AlertDialog.Builder(ADS_Activity.this);
        builder.setMessage(getString(R.string.condition)).setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent instagram = new Intent(Intent.ACTION_SEND);
                instagram.setPackage("com.instagram.android");
                instagram.setType("image/*");

                PackageManager packManager = getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(instagram, PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for (ResolveInfo resolveInfo : resolvedInfoList) {
                    if (resolveInfo.activityInfo.packageName.startsWith("com.instagram.android")) {
                        instagram.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name);
                        resolved = true;
                        break;
                    }
                }

                if (resolved) {
                    new MakePost().execute();
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.instagram.android"));
                    startActivity(intent);
                }

            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();



    }

    private class MakePost extends AsyncTask {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog = new ProgressDialog(ADS_Activity.this);
            progressDialog.setMessage("Posting");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(ADS_Activity.this);
            String email = userPrefs.getString("email", null);
            String password = userPrefs.getString("password", null);
            String instagram = userPrefs.getString("instagramLogin", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("offerId", post_id));
            params.add(new BasicNameValuePair("login", instagram));
            params.add(new BasicNameValuePair("stId", "1"));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/post/create", "POST", params);
            Log.d("CREATE", jsonObject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

         new OpenInstagram().execute();
        }
    }

    private class checkTime extends AsyncTask {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        long difference;
        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(ADS_Activity.this);
            String email = userPrefs.getString("email", null);
            String password = userPrefs.getString("password", null);
            String instagram = userPrefs.getString("instagramLogin", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("login", instagram));
            params.add(new BasicNameValuePair("stId", "1"));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/post/getLastPosting", "POST", params);
            Log.d("TIME", jsonObject.toString());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (jsonObject.has("error")) {
                //Toast.makeText(ADS_Activity.this, getResources().getString(R.string.postError), Toast.LENGTH_LONG).show();
            } else {
                String postTime = jsonObject.toString();
                postTime = postTime.substring(9, postTime.length() - 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date post;
                try {

                    post = sdf.parse(postTime);
                    long postTimeInMilli = post.getTime() + 43200000;
                    long currentTimeInMilli = System.currentTimeMillis();
                    long diff = -currentTimeInMilli + postTimeInMilli;
                    if (diff>1000*60*60*12){//compensate time difference b/w server and mobile phone. 2minute 30 seconds
                        diff-=120*1000+30000;
                    }
                    if (diff > 0) {
                        postAd.setEnabled(false);
                        new CountDownTimer(diff, 1000){

                            @Override
                            public void onTick(long diff) {
                                long H = (diff/ (1000 * 60 * 60)) % 24;
                                long m = (diff / (1000 * 60)) % 60;
                                long s = (diff / 1000) % 60;
                                String timeButton, hour,minute,second;
                                hour=""+H;
                                if(H<10){
                                    hour="0"+H;
                                }
                                minute=""+m;
                                if(m<10){
                                    minute="0"+m;
                                }
                                second=""+s;
                                if(s<10){
                                    second="0"+s;
                                }
                                timeButton=hour+":"+minute+":"+second;
                                postAd.setText(timeButton);
                            }

                            @Override
                            public void onFinish() {
                                postAd.setText(R.string.publish);
                                postAd.setEnabled(true);
                            }
                        }.start();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }



    }


    private class OpenInstagram extends AsyncTask {

        protected void onPreExecute(){
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            //Log.d("IMAGE", resultp.get(AdsFragment.TAG_PHOTO));
            String urlImage = poster;//resultp.get(AdsFragment.TAG_PHOTO);

            Intent instagram = new Intent(Intent.ACTION_SEND);
            instagram.setPackage("com.instagram.android");
            instagram.setType("image/*");


            try {
                URL url = new URL(urlImage);
                InputStream is = url.openConnection().getInputStream();
                Bitmap bmp = BitmapFactory.decodeStream(is);
                String image = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, title, "Share happy !");
                instagram.putExtra(Intent.EXTRA_STREAM, Uri.parse(image));
                instagram.putExtra(Intent.EXTRA_TEXT, title);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            startActivity(instagram);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            // COPY THE TEXT INTO CLIPBOARD
            //@SuppressWarnings("static-access")
            //ClipboardManager clipboard = (ClipboardManager) Context.getSystemService(Context.CLIPBOARD_SERVICE);
            //ClipData clip = ClipData.newPlainText("label", title);
            //clipboard.setPrimaryClip(clip);
            //new MakePost().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ADS_Activity.this, NSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(ADS_Activity.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ADS_Activity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("email");
            editor.remove("password");
            editor.commit();
            Intent intent = new Intent(ADS_Activity.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

