package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

import static android.icu.lang.UProperty.INT_START;

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
        if(filteredData==null)
            return 0;
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


        TextView topicName = null;
        LinearLayout linearLayout = null;


        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.fragment_listitem, null);



            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(t.topicName);
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            ArrayList<Tag> tg = t.tags;


            if(tg!=null) {
                for (int i = 0; i < tg.size(); i++) {
                    String text = tg.get(i).tagName + ": " + tg.get(i).context;
                    TextView tagView = beautifyTagView(text, getContext());
                    linearLayout.addView(tagView);

                }
            }
        }else{
            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(getItem(position).topicName);

            ArrayList<Tag> tg = getItem(position).tags;
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);

            linearLayout.removeAllViews();

            if(tg!=null) {
                for (int i = 0; i < tg.size(); i++) {
                    String text = tg.get(i).tagName + ": " + tg.get(i).context;
                    TextView tagView = beautifyTagView(text, getContext());
                    linearLayout.addView(tagView);

                }
            }
        }
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
                if (topic.topicName.toLowerCase(Locale.getDefault())
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
                Topic temp = list.get(i);
                filterableString = temp.topicName;
                if(temp.tags!=null) {
                    for (int j = 0; j < temp.tags.size(); j++) {
                        filterableString += temp.tags.get(j);
                    }

                }
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
    public void updateArray(){
        topicsWithTags = DatabaseHelper.getInstance(getContext()).getAllTopics();
        for(Topic t:topicsWithTags){
            Log.d("updated", t.topicName);
        }
    }

    public static TextView beautifyTagView(String text, Context context){
        TextView tagView = new TextView(context);
        final SpannableStringBuilder str = new SpannableStringBuilder(text);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(1.25f), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.LTGRAY), text.indexOf(":")+2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tagView.setText(str);
        tagView.setBackgroundResource(R.drawable.tagbg);
        tagView.setTextColor(Color.WHITE);
        tagView.setGravity(Gravity.CENTER);
        tagView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            lp.setMarginEnd(10);
        }
        tagView.setLayoutParams(lp);

        tagView.setPadding(15, 15, 15, 15);
        return tagView;
    }
}



