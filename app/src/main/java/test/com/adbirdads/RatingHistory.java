package test.com.adbirdads;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.ValueDependentColor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RatingHistory extends Activity {

    TextView ratingTV;
    int r1, r2, r3;
    String monthName;
    String[] month = new String[] {" ", " ", " "};
    int[] rating = new int[] {0, 0, 0};
    String[] monthes = new String[] {"Январь","Февраль","Март","Апрель"
            ,"Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new getRatingHistory().execute();
        setContentView(R.layout.activity_rating_history);
        getActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ratingPref = sharedPreferences.getString("rating", "0");

        ratingTV = (TextView) findViewById(R.id.moneyForPost);

        ratingTV.setText(ratingPref);


    }

    class getRatingHistory extends AsyncTask {

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(RatingHistory.this);
            String email = userPrefs.getString("email", null);
            String password = userPrefs.getString("password", null);
            String instagram = userPrefs.getString("instagramLogin", null);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("login", instagram));
            params.add(new BasicNameValuePair("stId", "1"));

            jsonObject = jsonParser.makeHttpRequest("http://api.adbird.net/index.php/surfer/getRatingHistory", "POST", params);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            try {
                JSONArray jsonArray = jsonObject.getJSONArray("months");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String ratingConvert = jsonObject.getString("rating");
                    Double d = Double.parseDouble(ratingConvert);
                    int temp;
                    temp = d.intValue();
                    String s = temp+"";
                    Log.d("rat", s+"");
                    rating[i] = Integer.parseInt(s);
                    monthName = jsonObject.getString("month");
                    String monthOlolo = " ";

                    if (monthName.equals("1")) {
                        monthOlolo = monthes[0];
                    } else if (monthName.equals("2")) {
                        monthOlolo = monthes[1];
                    } else if (monthName.equals("3")) {
                        monthOlolo = monthes[2];
                    } else if (monthName.equals("4")) {
                        monthOlolo = monthes[3];
                    } else if (monthName.equals("5")) {
                        monthOlolo = monthes[4];
                    } else if (monthName.equals("6")) {
                        monthOlolo = monthes[5];
                    } else if (monthName.equals("7")) {
                        monthOlolo = monthes[6];
                    } else if (monthName.equals("8")) {
                        monthOlolo = monthes[7];
                    } else if (monthName.equals("9")) {
                        monthOlolo = monthes[8];
                    } else if (monthName.equals("10")) {
                        monthOlolo = monthes[9];
                    } else if (monthName.equals("11")) {
                        monthOlolo = "Ноябрь";
                    } else if (monthName.equals("12")) {
                        monthOlolo = monthes[11];
                    }

                    Log.d("MONTH", monthOlolo);
                    month[i] = monthOlolo;


                }
                showGraph();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void showGraph() {
        GraphViewSeries.GraphViewSeriesStyle seriesStyle = new GraphViewSeries.GraphViewSeriesStyle();

        seriesStyle.setValueDependentColor(new ValueDependentColor() {
            @Override
            public int get(GraphViewDataInterface graphViewDataInterface) {
                return Color.rgb(196, 102, 55);
            }
        });

        GraphViewSeries ratingSeries = new GraphViewSeries("", seriesStyle, new GraphViewData[] {
                new GraphViewData(0, rating[0]),
                new GraphViewData(1, rating[1]) ,
                new GraphViewData(2, rating[2]),
        });


        GraphView graphView = new BarGraphView(RatingHistory.this, "");
        graphView.addSeries(ratingSeries);
        graphView.setHorizontalLabels(month);//new String[]{"Aвгуст", "Сентябрь", "Октябрь"});
        graphView.getGraphViewStyle().setGridStyle(GraphViewStyle.GridStyle.HORIZONTAL);
        graphView.getGraphViewStyle().setGridColor(R.color.grey_as_sasha);
        graphView.getGraphViewStyle().setVerticalLabelsColor(R.color.grey_as_sasha);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(getResources().getColor(R.color.grey_as_sasha));
        graphView.getGraphViewStyle().setNumVerticalLabels(5);
        graphView.getGraphViewStyle().setNumHorizontalLabels(3);


        //graphView.setViewPort(1, 3);
        graphView.getGraphViewStyle().setTextSize(getResources().getDimension(R.dimen.graphViewTextSize));

        LinearLayout layout = (LinearLayout) findViewById(R.id.graphLayout);
        layout.addView(graphView);
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
