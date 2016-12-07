package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetTopicDetail;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;
import rm.com.longpresspopup.PopupInflaterListener;
import rm.com.longpresspopup.PopupStateListener;

/**
 * Created by Ezgi on 12/7/2016.
 */


public class CustomTopicDetailAdapter extends ArrayAdapter<Relation> implements PopupInflaterListener, PopupStateListener{//, View.OnClickListener {

    View v;
    int topicId;
    public static ListView popupListView;
    LongPressPopup popup;


    public CustomTopicDetailAdapter(Context context, int resource, List<Relation> relations, int topicId) {
        super(context, resource, relations);
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


            rn = (TextView) v.findViewById(R.id.relation_name);
            iv = (ImageView) v.findViewById(R.id.arrow_direction_picture);
            t2n = (TextView) v.findViewById(R.id.topic2_name);

            if (r.topicFrom == topicId) {

                if(r.isBidirectional)
                    iv.setImageResource(R.drawable.two_arrow);

                else
                    iv.setImageResource(R.drawable.right_arrow);


            }else if (r.topicTo == topicId) {
                if(r.isBidirectional)
                    iv.setImageResource(R.drawable.two_arrow);
                else
                    iv.setImageResource(R.drawable.left_arrow);
            }

            rn.setText(r.relationName);
            t2n.setText(DatabaseHelper.getInstance(getContext()).getTopic(r.topicTo).topicName);



            //long press
            if (popup != null && popup.isRegistered()) {
                popup.unregister();
            }

            popup = new LongPressPopupBuilder(getContext())
                    .setTarget(v)
                    .setPopupView(R.layout.relation_dialog_view, this)
                    .setAnimationType(LongPressPopup.ANIMATION_TYPE_FROM_CENTER)
                    .setPopupListener(this)
                    .build();

            popup.register();

            //short press
           // v.setOnClickListener(this);




        }





        return v;
    }



    @Override
    public void onViewInflated(@Nullable String popupTag, View root) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());
        List<Relation> relations = databaseHelper.getAllRelations(topicId);
        CustomTopicDetailAdapter detailAdapter=new CustomTopicDetailAdapter(getContext(), R.layout.relation_dialog_view, relations, topicId);

        popupListView = (ListView) root.findViewById(R.id.relations_list);
        popupListView.setAdapter(detailAdapter);
    }

    @Override
    public void onPopupShow(@Nullable String popupTag) {

    }

    @Override
    public void onPopupDismiss(@Nullable String popupTag) {

    }

    /*@Override
    public void onClick(View view) {
        if (view.getId() == v.getId()) {
            new GetTopicDetail(MeancoApplication.SITE_URL, topicId, getContext()).execute();
            String message = DatabaseHelper.getInstance(getContext()).getTopic(topicId).topicName;
            // String topicId = adapter.getItem(position).topicId+"";
            Intent intent = new Intent(v.getContext(), TopicDetailActivity.class);
            intent.putExtra("activityTitle", message);
            // intent.putExtra("topicId", topicId);
            v.getContext().startActivity(intent);
        }
    }*/
}