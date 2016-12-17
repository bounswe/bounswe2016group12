package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TopicSearchActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by feper on 12/17/2016.
 */

public class ProfileTopicAdapter extends ArrayAdapter<Topic> {
    public static List<Topic> followedTopics = new ArrayList<>();
    public ProfileTopicAdapter(Context context, int resource) {
        super(context, resource);
        followedTopics.clear();
        for(Integer i:MeancoApplication.followedTopicList){
            Topic t = DatabaseHelper.getInstance(context).getTopic(i);
            followedTopics.add(t);
        }
    }

    @Override
    public int getCount(){
        if(followedTopics==null)
            return 0;
        else
            return followedTopics.size();
    }

    @Override
    public Topic getItem(int position){
        return followedTopics.get(position);
    }

    @Override
    public int getPosition(Topic item){
        return followedTopics.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Topic t = getItem(position);

        TextView topicName = null;
        LinearLayout linearLayout = null;
        ArrayList<Tag> tg;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.fragment_listitem, null);



            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(t.topicName);
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            tg = t.tags;

        }else{
            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(getItem(position).topicName);

            tg = getItem(position).tags;
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            linearLayout.removeAllViews();
        }

        if(tg!=null) {
            for (int i = 0; i < (tg.size() > 2 ? 2:tg.size()); i++) {
                String text = tg.get(i).tagName + ": " + tg.get(i).context;
                TextView tagView = Functions.beautifyTagView(text, getContext());
                linearLayout.addView(tagView);
            }
        }
        return v;
    }
    public void updateArray(){
        for(Topic t: followedTopics){
            this.add(t);
        }
    }
}
