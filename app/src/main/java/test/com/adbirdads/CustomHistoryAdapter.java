package test.com.adbirdads;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by air on 08.09.14.
 */
public class CustomHistoryAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    String textStatus;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public CustomHistoryAdapter(Context context,
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

    public View getView(final int position, final View convertView, ViewGroup parent) {

        TextView title, posting_date, payment, status;
        ImageView poster;
        RelativeLayout historyLayout;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.history_list_layout, parent, false);
        resultp = data.get(position);
        Log.d(resultp.get(HistoryFragment.TAG_DATA),resultp.get(HistoryFragment.TAG_COMMENTS));
        Log.d(resultp.get(HistoryFragment.TAG_COMPANYNAME),resultp.get(HistoryFragment.TAG_LIKES));
        Log.d(resultp.get(HistoryFragment.TAG_TITLE),resultp.get(HistoryFragment.TAG_STATUS));

        // Настраиваем текстовые поля
        title = (TextView) itemView.findViewById(R.id.titleLabel);
        posting_date = (TextView) itemView.findViewById(R.id.release_date_label);
        status = (TextView) itemView.findViewById(R.id.ratingLabel);
        historyLayout = (RelativeLayout) itemView.findViewById(R.id.historyLayout);

        if (resultp.get(HistoryFragment.TAG_STATUS).equals("1")) {
            textStatus = context.getResources().getString(R.string.notChecked);
            status.setTextColor(context.getResources().getColor(R.color.grey_as_sasha));
        } else if(resultp.get(HistoryFragment.TAG_STATUS).equals("2")) {
            textStatus = context.getResources().getString(R.string.published);
            status.setTextColor(context.getResources().getColor(R.color.green));
        } else {
            textStatus = context.getResources().getString(R.string.notFound);
            status.setTextColor(context.getResources().getColor(R.color.red));
        }

        // ImageView
        poster = (ImageView) itemView.findViewById(R.id.flag);

        title.setText(resultp.get(HistoryFragment.TAG_COMPANYNAME));
        posting_date.setText(resultp.get(HistoryFragment.TAG_DATA));
        //payment.setText(resultp.get(HistoryFragment.TAG_PAYMENT));
        status.setText(textStatus);

        imageLoader.DisplayImage(resultp.get(HistoryFragment.TAG_PHOTO), poster);

        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HistorySingleItem.class);
                intent.putExtra("title", resultp.get(HistoryFragment.TAG_TITLE));
                Log.d("title", resultp.get(HistoryFragment.TAG_TITLE));
                intent.putExtra("date", resultp.get(HistoryFragment.TAG_DATA));
                intent.putExtra("photo", resultp.get(HistoryFragment.TAG_PHOTO));
                intent.putExtra("status", textStatus);
                intent.putExtra("brand", resultp.get(HistoryFragment.TAG_COMPANYNAME));
                intent.putExtra("likes", resultp.get(HistoryFragment.TAG_LIKES));
                intent.putExtra("comments", resultp.get(HistoryFragment.TAG_COMMENTS));
                context.startActivity(intent);
            }
        });

        return itemView;
    }

    public void updateResults(ArrayList<HashMap<String, String>> results) {

        data = results;

        notifyDataSetChanged();

    }
}
