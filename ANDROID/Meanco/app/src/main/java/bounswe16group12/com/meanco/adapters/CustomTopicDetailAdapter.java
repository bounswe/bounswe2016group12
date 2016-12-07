package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

/**
 * Created by Ezgi on 12/7/2016.
 */


public class CustomTopicDetailAdapter extends ArrayAdapter<Relation> {

    List<Relation> relations;
    int topicId;

    public CustomTopicDetailAdapter(Context context, int resource, List<Relation> relations, int topicId) {
        super(context, resource, relations);
        this.topicId = topicId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Relation r = getItem(position);


        TextView rn;
        ImageView iv;


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.relation_dialog_item, null);


            rn = (TextView) v.findViewById(R.id.relation_name);
            iv = (ImageView) v.findViewById(R.id.arrow_direction_picture);

            //TODO: NEDEN OLMUYOR?
            if (r.topicFrom == topicId) {
                rn.setText(r.relationName);
                Log.d("NEDENNN", rn.getText().toString());
                if(r.isBidirectional)
                    iv.setImageResource(R.drawable.two_arrow);

                else
                    iv.setImageResource(R.drawable.right_arrow);


            }else if (r.topicTo == topicId) {
                rn.setText(r.relationName);
                if(r.isBidirectional)
                    iv.setImageResource(R.drawable.two_arrow);
                else
                    iv.setImageResource(R.drawable.left_arrow);
            }


        }
        return v;
    }
}