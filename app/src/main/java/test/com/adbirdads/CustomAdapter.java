package test.com.adbirdads;

/**
 * Created by Const on 04.07.2014.
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class CustomAdapter extends BaseAdapter  {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    HashMap<String, String> resultp = new HashMap<String, String>();

    String post_id;

    int secs, mins;

    /*private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            Log.i("COUNTDOWN", "Countdown seconds remaining: " +  millisUntilFinished / 1000);
            int secs = (int) (millisUntilFinished / 1000);
            int mins = secs / 60;
            secs = secs % 60; // or whatever method used to update your GUI fields
        }
    };*/

    public CustomAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Button buttonShare;
        TextView title;
        ImageView poster;
        View itemView = null;
        Long time = AdsFragment.getTime();
        Boolean finished = AdsFragment.getPosted();

        if (convertView == null) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.ads_layout, parent, false);
        } else {
            itemView = convertView;
        }

        resultp = data.get(position);

        // Настраиваем текстовые поля
        title = (TextView) itemView.findViewById(R.id.title);
        buttonShare = (Button) itemView.findViewById(R.id.postButton);

        // ImageView
        poster = (ImageView) itemView.findViewById(R.id.adImage);
        post_id = resultp.get(AdsFragment.TAG_ID);
        title.setText(resultp.get(AdsFragment.TAG_BRAND));
        imageLoader.DisplayImage(resultp.get(AdsFragment.TAG_PHOTO), poster);
        buttonShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resultp = data.get(position);
                Intent intent = new Intent(context, ADS_Activity.class);
                intent.putExtra("id", resultp.get(AdsFragment.TAG_ID));
                intent.putExtra("brand", resultp.get(AdsFragment.TAG_BRAND));
                intent.putExtra("title", resultp.get(AdsFragment.TAG_TITLE));
                intent.putExtra("date", resultp.get(AdsFragment.TAG_DATE));
                intent.putExtra("poster", resultp.get(AdsFragment.TAG_PHOTO));
                intent.putExtra("amount", resultp.get(AdsFragment.TAG_AMOUNT));
                context.startActivity(intent);
                /*Boolean posted = AdsFragment.getPosted();
                if (posted == false) {
                    context.startService(new Intent(context, BroadcastService.class));
                    Log.i("SERVICE", "Started service");
                    Log.d("POSTID", post_id);
                    new OpenInstagram().execute();
                    new MakePost().execute();
                    //updateGUI(buttonShare);

                } else {
                    Log.i("POST", "WAS POSTED!!!");
                    new AlertDialog.Builder(context).setTitle("LALKA")
                            .setMessage("LALKA").setCancelable(true).show();
                }
                Intent intent = new Intent(context, ADS_Activity.class);
                //intent.putExtra();

*/
            }
        });
        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resultp = data.get(position);
                Intent intent = new Intent(context, ADS_Activity.class);
                intent.putExtra("id", resultp.get(AdsFragment.TAG_ID));
                intent.putExtra("brand", resultp.get(AdsFragment.TAG_BRAND));
                intent.putExtra("title", resultp.get(AdsFragment.TAG_TITLE));
                intent.putExtra("date", resultp.get(AdsFragment.TAG_DATE));
                intent.putExtra("poster", resultp.get(AdsFragment.TAG_PHOTO));
                intent.putExtra("amount", resultp.get(AdsFragment.TAG_AMOUNT));
                context.startActivity(intent);
            }
        });

        /*if(finished == false) {
            buttonShare.setText("Post");
        } else {
            int secs = (int) (time / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            buttonShare.setText(" " + mins + " : " + String.format("%02d", secs));
        }*/


        return itemView;
    }

    private void updateGUI(final Button button) {
        //Boolean posted = AdsFragment.getPosted();
        //button.setText(" " + mins + " : " + String.format("%02d", secs));

    }

    CountDownTimer cdt = new CountDownTimer(20000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            secs = (int) (millisUntilFinished / 1000);
            mins = secs / 60;
            secs = secs % 60;
            //updateGUI();
        }

        @Override
        public void onFinish() {

        }

    };

    public void updateResults(ArrayList<HashMap<String, String>> results) {
    	
    	data = results;
    	
    	notifyDataSetChanged();
		
	}
}
