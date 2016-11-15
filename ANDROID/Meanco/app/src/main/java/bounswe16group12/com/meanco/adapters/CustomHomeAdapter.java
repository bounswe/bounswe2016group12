package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

public class CustomHomeAdapter extends ArrayAdapter<Topic> {

    List<Topic> topicsWithTags;

    public CustomHomeAdapter(Context context, int resource, List<Topic> topicsWithTags) {
        super(context, resource, topicsWithTags);
        this.topicsWithTags = topicsWithTags;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;

        final View view = convertView;
        View v = convertView;
        Topic t = topicsWithTags.get(position);


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.fragment_listitem, null);

        }


        if (t != null) {


            TextView topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(t.getTopicName());

            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            ArrayList<Tag> tg = t.getTags();



            for(int i=0; i<tg.size(); i++){

                    TextView tagView = new TextView(getContext());

                    tagView.setText(tg.get(i).getTagName());
                    tagView.setBackgroundResource(R.drawable.tagbg);
                    tagView.setTextColor(Color.WHITE);
                    tagView.setGravity(Gravity.CENTER);
                    tagView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tagView.setPadding(15, 15, 15, 15);
                    linearLayout.addView(tagView);
            }



        }
        return v;
    }
}
