package test.com.adbirdads;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class HistorySingleItem extends Activity {

    String title, date, brand, status, photo, comments, likes;
    TextView titleText;
    TextView textDate;
    TextView textView3;
    TextView textView4;
    TextView likeTextView;
    TextView commentstextview;
    ImageView photoImage;

    ImageLoader imageLoader = new ImageLoader(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historysingleitem);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        titleText = (TextView) findViewById(R.id.titleText);
        textDate = (TextView) findViewById(R.id.textDate);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        likeTextView = (TextView) findViewById(R.id.likeTextView);
        commentstextview = (TextView) findViewById(R.id.commentstextview);

        photoImage = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        brand = intent.getStringExtra("brand");
        status = intent.getStringExtra("status");
        photo = intent.getStringExtra("photo");
        comments = intent.getStringExtra("comments");
        likes = intent.getStringExtra("likes");

        date = date.replace("-", ".");
        String[] parts = new String[2];
        parts = date.split(" ");

        date = parts[0];

        titleText.setText(brand);
        textDate.setText(date);
        textView3.setText(title);
        textView4.setText(status);
        likeTextView.setText(likes);
        commentstextview.setText(comments);

        /*if (status.equals("Ожидается")) {
            textStatus = context.getResources().getString(R.string.notChecked);
            status.setTextColor(context.getResources().getColor(R.color.grey_as_sasha));
        } else if(status.equals("оплачено")) {
            textStatus = context.getResources().getString(R.string.published);
            status.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            textStatus = context.getResources().getString(R.string.notFound);
            status.setTextColor(context.getResources().getColor(R.color.red));
        }*/

        imageLoader.DisplayImage(photo, photoImage);

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
            Intent intent = new Intent(HistorySingleItem.this, NSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(HistorySingleItem.this, HelpActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HistorySingleItem.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("email");
            editor.remove("password");
            editor.commit();
            Intent intent = new Intent(HistorySingleItem.this, Login.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
