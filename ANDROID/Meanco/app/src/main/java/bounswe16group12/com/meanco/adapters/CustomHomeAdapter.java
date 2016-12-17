package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * An adapter which populates topics list view on home page.
 * Created by Ezgi on 12/10/2016.
 */

public class CustomHomeAdapter extends ArrayAdapter<Topic>{

    public static List<Topic> filteredData;

    public CustomHomeAdapter(Context context, int resource, List<Topic> topicsWithTags) {
        super(context, resource, topicsWithTags);
        this.filteredData = topicsWithTags;
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

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.fragment_listitem, null);
            topicName = (TextView) v.findViewById(R.id.topicitem);
            topicName.setText(t.topicName);

            ArrayList<Tag> tg = t.tags;
            linearLayout = (LinearLayout) v.findViewById(R.id.linearlayout);
            linearLayout.removeAllViews();

            LinearLayout.LayoutParams layparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layparam.setMargins(0,0,0,2);
            linearLayout.setLayoutParams(layparam);

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
        filteredData = DatabaseHelper.getInstance(getContext()).getAllTopics();
        for(Topic t:filteredData)
            this.add(t);

    }


}



