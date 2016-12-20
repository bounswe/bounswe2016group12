package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TagSearchActivity;
import bounswe16group12.com.meanco.objects.Tag;

/**
 * An adapter which populates tags list view with Wikidata tags when user wants to add tags to a topic.
 * Created by Ezgi on 12/7/2016.
 */

public class TagSearchAdapter extends ArrayAdapter<Tag>{
    public static List <Tag> wikiTags = new ArrayList<>();

    public TagSearchAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount(){
        if(wikiTags==null)
            return 0;
        else
            return wikiTags.size();
    }

    @Override
    public Tag getItem(int position){
        return wikiTags.get(position);
    }

    @Override
    public int getPosition(Tag item){
        return wikiTags.indexOf(item);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        final Tag t = getItem(position);
        final CheckedTextView checkedTextView;


            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tag_search_item, null);


            checkedTextView  = (CheckedTextView) v.findViewById(R.id.checkedTextView);

            if(TagSearchActivity.checkedTags.indexOf(t) != -1)
                checkedTextView.setChecked(true);
            else
                checkedTextView.setChecked(false);

            String text = t.tagName + ": " + t.context;

            final SpannableStringBuilder str = new SpannableStringBuilder(text);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new RelativeSizeSpan(1.25f), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.GRAY), text.indexOf(":")+2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            checkedTextView.setText(str);

        return v;
    }

    public void updateArray(){
        for(Tag t: wikiTags){
            this.add(t);
        }
    }


}
