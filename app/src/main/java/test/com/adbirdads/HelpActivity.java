package test.com.adbirdads;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HelpActivity extends Activity implements View.OnClickListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    RelativeLayout writeToUsLayout, howItWorksLayout, FAQLayout, userAgreementLayout, termsOfService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(getString(R.string.action_help));

        writeToUsLayout = (RelativeLayout) findViewById(R.id.writeToUsLayout);
        howItWorksLayout = (RelativeLayout) findViewById(R.id.howItWorksLayout);
        FAQLayout = (RelativeLayout) findViewById(R.id.FAQLayout);
        userAgreementLayout = (RelativeLayout) findViewById(R.id.userAgreementLayout);
        termsOfService=(RelativeLayout)findViewById(R.id.termsOfService);

        writeToUsLayout.setOnClickListener(this);
        howItWorksLayout.setOnClickListener(this);
        FAQLayout.setOnClickListener(this);
        userAgreementLayout.setOnClickListener(this);
        termsOfService.setOnClickListener(this);

        // get the listview
        //expListView = (ExpandableListView) findViewById(R.id.expandableLV);

        // preparing list data
        //prepareListData();

        //listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        //expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Написать нам");
        listDataHeader.add("Как работает AdBird");
        listDataHeader.add("FAQ");
        listDataHeader.add("Пользовательское соглашение");
        listDataHeader.add("Правила работы сервиса");

        // Adding child data
        List<String> writeUs = new ArrayList<String>();
        writeUs.add("Наш email: wakeup@gmail.com");


        List<String> howItWorks = new ArrayList<String>();
        howItWorks.add("Работает хорошо! Очень даже! 11 из 10");

        List<String> faq = new ArrayList<String>();
        faq.add("Все же просто! Какие вопросы?");

        List<String> userAgreement = new ArrayList<String>();
        userAgreement.add("Вы все заранее уже со всем согласны!");

        List<String> termsOfService=new ArrayList<String>();
        termsOfService.add("Сервис");

        listDataChild.put(listDataHeader.get(0), writeUs); // Header, Child data
        listDataChild.put(listDataHeader.get(1), howItWorks);
        listDataChild.put(listDataHeader.get(2), faq);
        listDataChild.put(listDataHeader.get(3), userAgreement);
        listDataChild.put(listDataHeader.get(4),termsOfService);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HelpActivity.this, NSettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            //Intent intent = new Intent(HelpActivity.this, HelpActivity.class);
           // startActivity(intent);
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(HelpActivity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove("email");
            editor.remove("password");
            editor.commit();
            Intent intent = new Intent(HelpActivity.this, Login.class);
            startActivity(intent);
            return true;
        }
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intentMenu = new Intent(getBaseContext(),
                        MainActivity.class);
                startActivity(intentMenu);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.writeToUsLayout :
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@adbird.com"});
                startActivity(Intent.createChooser(emailIntent, "Send email"));
                break;
            case R.id.howItWorksLayout :
                Intent tutorialIntent = new Intent(HelpActivity.this, Tutorial.class);
                startActivity(tutorialIntent);
                break;
            case R.id.FAQLayout :
                Intent faqIntent = new Intent(HelpActivity.this, FaqActivity.class);
                startActivity(faqIntent);
                break;
            case R.id.userAgreementLayout :
                Intent userAgreementIntent = new Intent(HelpActivity.this, UserAgreement.class);
                startActivity(userAgreementIntent);
                break;
            case R.id.termsOfService :
                Intent termsOfServiceIntent = new Intent(HelpActivity.this, TermsOfService.class);
                startActivity(termsOfServiceIntent);
                break;
            default:
                break;

        }
    }
}
