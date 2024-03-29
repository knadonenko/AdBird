package test.com.adbirdads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by air on 26.10.14.
 */
public class LeadrsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    String textStatus;
    HashMap<String, String> resultp = new HashMap<String, String>();
    int[] imageLeaders = new int[] {R.drawable.leader_1, R.drawable.leader_2, R.drawable.leader_3,
            R.drawable.leader_4, R.drawable.leader_5, R.drawable.leader_6, R.drawable.leader_7,
            R.drawable.leader_8, R.drawable.leader_9, R.drawable.leader_10};

    public LeadrsAdapter(Context context,
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

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        TextView leaderName, LeaderRating;
        ImageView poster;
        RelativeLayout historyLayout;
        ImageView positionImage;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.leaders_layout, parent, false);
        resultp = data.get(position);
        //leaderPosition(position);

        // Настраиваем текстовые поля
        leaderName = (TextView) itemView.findViewById(R.id.textView);
        positionImage = (ImageView) itemView.findViewById(R.id.positionImage);
        poster = (ImageView) itemView.findViewById(R.id.leader_avatar);
        LeaderRating = (TextView) itemView.findViewById(R.id.textView2);
        String name = resultp.get(LeadersFragment.TAG_LEADERNAME);
        String surname = resultp.get(LeadersFragment.TAG_LEADERSURNAME);
        String username = name + " " + surname;
        String rating = resultp.get(LeadersFragment.TAG_LEADERRATING);
        Double userRating = Double.valueOf(rating);
        leaderName.setText(username);
        LeaderRating.setText(String.format("%.0f", userRating));
        imageLoader.DisplayImage(resultp.get(LeadersFragment.TAG_PHOTO), poster);

        if (position == 0) {
            positionImage.setImageResource(R.drawable.leader_1);
        } if (position == 1) {
            positionImage.setImageResource(R.drawable.leader_2);
        } if (position == 2) {
            positionImage.setImageResource(R.drawable.leader_3);
        } if (position == 3) {
            positionImage.setImageResource(R.drawable.leader_4);
        } if (position == 4) {
            positionImage.setImageResource(R.drawable.leader_5);
        } if (position == 5) {
            positionImage.setImageResource(R.drawable.leader_6);
        } if (position == 6) {
            positionImage.setImageResource(R.drawable.leader_7);
        } if (position == 7) {
            positionImage.setImageResource(R.drawable.leader_8);
        } if (position == 8) {
            positionImage.setImageResource(R.drawable.leader_9);
        } if (position == 9) {
            positionImage.setImageResource(R.drawable.leader_10);
        }

        return itemView;
    }

    public void leaderPosition(int pos) {

        switch (pos) {
            case 0 :
                //positionImage.setImageResource(R.drawable.leader_1);
                break;
            /*case 1 :
                positionImage.setImageResource(imageLeaders[1]);
                break;
            case 2 :
                positionImage.setImageResource(imageLeaders[2]);
                break;
            case 3 :
                positionImage.setImageResource(imageLeaders[3]);
                break;
            case 4 :
                positionImage.setImageResource(imageLeaders[4]);
                break;
            case 5 :
                positionImage.setImageResource(imageLeaders[5]);
                break;
            case 6 :
                positionImage.setImageResource(imageLeaders[6]);
                break;
            case 7 :
                positionImage.setImageResource(imageLeaders[7]);
                break;
            case 8 :
                positionImage.setImageResource(imageLeaders[8]);
                break;
            case 9 :
                positionImage.setImageResource(imageLeaders[9]);
                break;*/
            default:
                break;
        }
    }
    @Override
    public boolean isEnabled(int position) {

        return false;
    }
}
