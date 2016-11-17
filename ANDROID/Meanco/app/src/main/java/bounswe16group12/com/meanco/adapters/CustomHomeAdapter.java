package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

public class CustomHomeAdapter extends ArrayAdapter<Topic> implements  Filterable{

    List<Topic> topicsWithTags;
    List<Topic> filteredData;
    private ItemFilter mFilter = new ItemFilter();

    public CustomHomeAdapter(Context context, int resource, List<Topic> topicsWithTags) {
        super(context, resource, topicsWithTags);
        this.filteredData = topicsWithTags;
        this.topicsWithTags = topicsWithTags;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Topic getItem(int position){
        return filteredData.get(position);
    }

    @Override
    public int getPosition(Topic item){
        return filteredData.indexOf(item);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Topic t = getItem(position);
        Log.i("t hillary?", t.getTopicName());


        TextView topicName = null;
        LinearLayout linearLayout = null;


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.fragment_listitem, null);

            // if (t != null) {


            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(t.getTopicName());
            Log.i("tn", t.getTopicName());
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            ArrayList<Tag> tg = t.getTags();


            for (int i = 0; i < tg.size(); i++) {

                TextView tagView = new TextView(getContext());

                tagView.setText(tg.get(i).getTagName());
                tagView.setBackgroundResource(R.drawable.tagbg);
                tagView.setTextColor(Color.WHITE);
                tagView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMarginEnd(10);
                tagView.setLayoutParams(lp);

                tagView.setPadding(15, 15, 15, 15);
                linearLayout.addView(tagView);

            }
        }else{
            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(getItem(position).getTopicName());

            ArrayList<Tag> tg = getItem(position).getTags();
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            linearLayout.removeAllViews();

            for (int i = 0; i < tg.size(); i++) {

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
        //}
        return v;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        topicsWithTags.clear();
        if (charText.length() == 0) {
            topicsWithTags.addAll(filteredData);
        } else {
            for (Topic topic : filteredData) {
                if (topic.getTopicName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    topicsWithTags.add(topic);
                }
            }
        }
        notifyDataSetChanged();
    }


    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Topic> list = topicsWithTags;

            int count = list.size();
            final ArrayList<Topic> nlist = new ArrayList<>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getTopicName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredData = (ArrayList<Topic>) results.values;
            if (results.count > 0)
            {
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }

        }


    }
}



