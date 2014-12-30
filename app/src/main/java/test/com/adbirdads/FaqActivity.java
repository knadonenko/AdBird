package test.com.adbirdads;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FaqActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    boolean aboutMoney;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();
        aboutMoney=i.getBooleanExtra("aboutmoney", false);


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableLV);


        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        if(aboutMoney){
            //Log.d("CONFIRMED", "YEAH");
            expListView.setSelectedChild(13,1,true);
            expListView.expandGroup(13);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getResources().getString(R.string.adbird_question));
        listDataHeader.add(getResources().getString(R.string.whosurfer));
        listDataHeader.add(getResources().getString(R.string.howitworks));
        listDataHeader.add(getResources().getString(R.string.post_a_post));
        listDataHeader.add(getResources().getString(R.string.whatIsBird));
        listDataHeader.add(getResources().getString(R.string.increaseBirds));
        listDataHeader.add(getResources().getString(R.string.whyRatingLower));
        listDataHeader.add(getResources().getString(R.string.rewards_question));
        listDataHeader.add(getResources().getString(R.string.getMoney_question));
        listDataHeader.add(getResources().getString(R.string.partsMoney_question));
        listDataHeader.add(getResources().getString(R.string.taxes_question));
        listDataHeader.add(getResources().getString(R.string.comission_question));
        listDataHeader.add(getResources().getString(R.string.why_walletOne_question));
        listDataHeader.add(getResources().getString(R.string.price_question));
        listDataHeader.add(getResources().getString(R.string.once_question));
        listDataHeader.add(getResources().getString(R.string.twoCategories_qeustions));
        listDataHeader.add(getResources().getString(R.string.posts_question));
        listDataHeader.add(getResources().getString(R.string.filters_question));
        listDataHeader.add(getResources().getString(R.string.text_question));

        // Adding child data
        List<String> writeUs = new ArrayList<String>();
        writeUs.add(getResources().getString(R.string.adbird_answer));

        List<String> howItWorks = new ArrayList<String>();
        howItWorks.add(getResources().getString(R.string.whosurfer_answer));

        List<String> faq = new ArrayList<String>();
        faq.add(getResources().getString(R.string.howitworks_answer));

        List<String> post_a_post_answer = new ArrayList<String>();
        post_a_post_answer.add(getResources().getString(R.string.post_a_post_answer));

        List<String> whatIsBird_answer = new ArrayList<String>();
        whatIsBird_answer.add(getResources().getString(R.string.whatIsBird_answer));

        List<String> increaseBirds_answer = new ArrayList<String>();
        increaseBirds_answer.add(getResources().getString(R.string.increaseBirds_answer));

        List<String> whyRatingLower_answer = new ArrayList<String>();
        whyRatingLower_answer.add(getResources().getString(R.string.whyRatingLower_answer));

        List<String> rewards_answer = new ArrayList<String>();
        rewards_answer.add(getResources().getString(R.string.rewards_answer));

        List<String> getMoney_answer = new ArrayList<String>();
        getMoney_answer.add(getResources().getString(R.string.getMoney_answer));

        List<String> partsMoney_answer = new ArrayList<String>();
        partsMoney_answer.add(getResources().getString(R.string.partsMoney_answer));

        List<String> taxes_answer = new ArrayList<String>();
        taxes_answer.add(getResources().getString(R.string.taxes_answer));

        List<String> comission_answer = new ArrayList<String>();
        comission_answer.add(getResources().getString(R.string.comission_answer));

        List<String> why_walletOne_answer = new ArrayList<String>();
        why_walletOne_answer.add(getResources().getString(R.string.why_walletOne_answer));

        List<String> price_answer = new ArrayList<String>();
        price_answer.add(getResources().getString(R.string.price_answer));

        List<String> once_answer = new ArrayList<String>();
        once_answer.add(getResources().getString(R.string.once_answer));

        List<String> twoCategories_answer = new ArrayList<String>();
        twoCategories_answer.add(getResources().getString(R.string.twoCategories_answer));

        List<String> posts_answer = new ArrayList<String>();
        posts_answer.add(getResources().getString(R.string.posts_answer));

        List<String> filters_answer = new ArrayList<String>();
        filters_answer.add(getResources().getString(R.string.filters_answer));

        List<String> text_answer = new ArrayList<String>();
        text_answer.add(getResources().getString(R.string.text_answer));

        listDataChild.put(listDataHeader.get(0), writeUs); // Header, Child data
        listDataChild.put(listDataHeader.get(1), howItWorks);
        listDataChild.put(listDataHeader.get(2), faq);
        listDataChild.put(listDataHeader.get(3), post_a_post_answer);
        listDataChild.put(listDataHeader.get(4), whatIsBird_answer);
        listDataChild.put(listDataHeader.get(5), increaseBirds_answer);
        listDataChild.put(listDataHeader.get(6), whyRatingLower_answer);
        listDataChild.put(listDataHeader.get(7), rewards_answer);
        listDataChild.put(listDataHeader.get(8), getMoney_answer);
        listDataChild.put(listDataHeader.get(9), partsMoney_answer);
        listDataChild.put(listDataHeader.get(10), taxes_answer);
        listDataChild.put(listDataHeader.get(11), comission_answer);
        listDataChild.put(listDataHeader.get(12), why_walletOne_answer);
        listDataChild.put(listDataHeader.get(13), price_answer);
        listDataChild.put(listDataHeader.get(14), once_answer);
        listDataChild.put(listDataHeader.get(15), twoCategories_answer);
        listDataChild.put(listDataHeader.get(16), posts_answer);
        listDataChild.put(listDataHeader.get(17), filters_answer);
        listDataChild.put(listDataHeader.get(18), text_answer);
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

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);

    }
}
