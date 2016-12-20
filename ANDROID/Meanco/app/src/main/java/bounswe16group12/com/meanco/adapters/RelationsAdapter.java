package bounswe16group12.com.meanco.adapters;

import android.content.Context;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Relation;

/**
 *
 * An adapter which populates relations list view on long press of a topic.
 * Created by Ezgi on 12/7/2016.
 */




public class RelationsAdapter extends ArrayAdapter<Relation>{

    View v;
    int topicId;
    List<Relation> relations;

    public RelationsAdapter(Context context, int resource, List<Relation> relations, int topicId) {
        super(context, resource, relations);
        this.relations = relations;
        this.topicId = topicId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        v = convertView;
        Relation r = getItem(position);

        TextView rn;
        ImageView iv;
        TextView t2n;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.relation_dialog_item, null);
        }

            rn = (TextView) v.findViewById(R.id.relation_name);
            iv = (ImageView) v.findViewById(R.id.arrow_direction_picture);
            t2n = (TextView) v.findViewById(R.id.topic2_name);

            if (r.topicFrom == topicId) {
                t2n.setText(DatabaseHelper.getInstance(getContext()).getTopic(r.topicTo).topicName);

                if(r.isBidirectional)
                    iv.setImageResource(R.drawable.two_arrow);

                else
                    iv.setImageResource(R.drawable.right_arrow);
                rn.setText(r.relationName);
            }else if (r.topicTo == topicId) {
                t2n.setText(DatabaseHelper.getInstance(getContext()).getTopic(r.topicFrom).topicName);

                if(r.isBidirectional)
                    iv.setImageResource(R.drawable.two_arrow);
                else
                    iv.setImageResource(R.drawable.left_arrow);
                rn.setText(r.relationName);
            }
            else{
                Log.i("RELATION ADAPTER", "WRONG INPUT");
            }
        Log.i("DETAIL" ,"POS : " + position + " REL : " + r.toString());
        return v;
    }

}