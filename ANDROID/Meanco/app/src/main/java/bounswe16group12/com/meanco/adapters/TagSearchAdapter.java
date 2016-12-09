package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TagSearchActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import rm.com.longpresspopup.LongPressPopup;
import rm.com.longpresspopup.LongPressPopupBuilder;

/**
 * Created by Ezgi on 12/7/2016.
 */

public class TagSearchAdapter extends ArrayAdapter<Tag>{
    public static List <Tag> wikiTags = new ArrayList<>();
   // static List <Tag> checked = new ArrayList<>();

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Tag t = getItem(position);


        TextView tagString;
        CheckBox checkBox;


       // if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tag_search_item, null);


            tagString = (TextView) v.findViewById(R.id.tag_string);
            checkBox = (CheckBox) v.findViewById(R.id.tag_check);
            String text = t.tagName + ": " + t.context;

            final SpannableStringBuilder str = new SpannableStringBuilder(text);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new RelativeSizeSpan(1.25f), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.LTGRAY), text.indexOf(":")+2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            tagString.setText(str);

            if(checkBox.isChecked()){
                TagSearchActivity.checkedTags.add(t);
            }





        //}





        return v;
    }

    public void updateArray(){
        for(Tag t: wikiTags){
            this.add(t);
        }
    }
}
