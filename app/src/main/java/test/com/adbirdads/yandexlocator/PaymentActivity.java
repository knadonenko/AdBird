package test.com.adbirdads.yandexlocator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import test.com.adbirdads.InformationFragment;
import test.com.adbirdads.R;

public class PaymentActivity extends Activity {

    private WebView browser;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        email = sp.getString("email", null);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        browser = (WebView) findViewById(R.id.webView);
        browser.setWebViewClient(new MyBrowser());
        openURL();
    }

    public void openURL() {
        if (email != null) {
            String url = "http://pay.adbird.com/Views/Private/?userEmail=" + email;
            browser.getSettings().setLoadsImagesAutomatically(true);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            browser.loadUrl(url);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void backToProfile() {
            Intent intent = new Intent(PaymentActivity.this, InformationFragment.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
