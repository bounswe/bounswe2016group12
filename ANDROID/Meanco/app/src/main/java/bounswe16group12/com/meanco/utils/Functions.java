package bounswe16group12.com.meanco.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Topic;

/**
 * Created by Ezgi on 12/13/2016.
 */

public class Functions {


    public static boolean filterData(final String newText, ArrayAdapter adapter, List<Topic> adapterTopics, Context context){

        DatabaseHelper db = DatabaseHelper.getInstance(context);
        List<Topic> topics;
        if (TextUtils.isEmpty(newText)) {
            topics = db.getAllTopics();
            if(adapterTopics.size() == topics.size())
                return true;
        } else {
            topics = db.getTopicsContainsText(newText);
        }

        Collections.sort(topics, new Comparator() {

            @Override
            public int compare(Object o, Object t1) {
                return ((Topic) o).topicName.indexOf(newText) - ((Topic) t1).topicName.indexOf(newText);
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }
        });
        adapterTopics.clear();
        adapterTopics.addAll(topics);
        adapter.notifyDataSetChanged();
        return true;
    }

    public static TextView beautifyTagView(String text, Context context){
        TextView tagView = new TextView(context);
        final SpannableStringBuilder str = new SpannableStringBuilder(text);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(1.25f), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.LTGRAY), text.indexOf(":")+2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        tagView.setText(str);
        tagView.setTextSize(12.0f);
        tagView.setBackgroundResource(R.drawable.tagbg);
        tagView.setTextColor(Color.WHITE);
        tagView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(2,0,0,3);

        tagView.setLayoutParams(lp);

        tagView.setPadding(15, 0, 15, 0);
        return tagView;
    }

    public static int getUserId(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        return preferences.getInt("UserId", -1);
    }

    public static void setUserId(int userId,Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putInt("UserId", userId);
        editor.commit();
    }

    public static void clearUserPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
