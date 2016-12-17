package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TopicSearchActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * An adapter which populates topic list view while choosing two topics in creating a relation.
 * Created by Ezgi on 12/10/2016.
 */

public class TopicSearchAdapter extends ArrayAdapter <Topic> {

    public static int idFrom, idTo;
    public static List<Topic> relationTopics;
    public TopicSearchAdapter(Context context, int resource) {
        super(context, resource);
        relationTopics = DatabaseHelper.getInstance(context).getAllTopics();

        /**
         * If user chose first topic, do not show that topic in the second list of topics
         * for the second topic (a topic cannot have a relation to itself).
         */
        if(!TopicSearchActivity.fromOrTo.equals("from")){
            for(int i=0; i<relationTopics.size(); i++){
                if(relationTopics.get(i).topicId==idFrom) {
                    relationTopics.remove(relationTopics.get(i));
                    break;
                }
            }
        }
    }

    @Override
    public int getCount(){
        if(relationTopics==null)
            return 0;
        else
            return relationTopics.size();
    }

    @Override
    public Topic getItem(int position){
        return relationTopics.get(position);
    }

    @Override
    public int getPosition(Topic item){
        return relationTopics.indexOf(item);
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
            for (int i = 0; i < tg.size(); i++) {
                String text = tg.get(i).tagName + ": " + tg.get(i).context;
                TextView tagView = Functions.beautifyTagView(text, getContext());
                linearLayout.addView(tagView);
            }
        }
        return v;
    }
    public void updateArray(){
        for(Topic t: relationTopics){
            this.add(t);
        }
    }
}


