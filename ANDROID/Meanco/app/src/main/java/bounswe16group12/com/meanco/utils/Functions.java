package bounswe16group12.com.meanco.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wooplr.spotlight.SpotlightView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.activities.LoginActivity;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.activities.TopicSearchActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Topic;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Ezgi on 12/13/2016.
 */

public class Functions {


    /**
     * Sort topics on search.
     * @param newText
     * @param adapter
     * @param adapterTopics
     * @param context
     * @return
     */
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

        /**
         * Sorts topics according to index occurrance of a string inside topicnames.
         * Ex: search ay with topics {selenay, ayben, onat}, result is {ayben, selenay}
         */
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

    /**
     * Makes textview of a tag seem like a tag on github.
     * @param text Text to be beautified.
     * @param context
     * @return beautified textview
     */
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
        lp.setMargins(2,0,0,11);

        tagView.setLayoutParams(lp);

        tagView.setPadding(15, 0, 15, 0);
        return tagView;
    }

    /**
     * If user is not logged in, this alert is shown whenever she tries to create content.
     * @param context
     */
    public static void showNotLoggedInAlert(final Context context){

        final TextView alert = new TextView(context);
        alert.setText("You have to login to complete this action.");
        alert.setPadding(30,30,30,30);

        new AlertDialog.Builder(context)
                .setTitle("Not logged in!")
                .setView(alert)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                }).show();
    }

    /**
     * @param context
     * @return user id if user is logged in, -1 if not
     */
    public static int getUserId(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE);
        return preferences.getInt("UserId", -1);
    }

    public static void setUserId(int userId,Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
       // editor.clear();
        editor.putInt("UserId", userId);
        editor.commit();
    }

    public static String getUsername(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        return preferences.getString("Username",null);
    }

    public static void setUsername(String username,Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username", username);
        editor.commit();
    }

    /**
     * Clear user preferences if user logs out.
     * @param context
     */
    public static void clearUserPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Show spotlight if user enters the app for the first time.
     * @param tvText title
     * @param subheadingTvText explanation
     * @param view View to be spotlighted
     * @param activity
     * @param usageId Unique for every spotlight.
     */
    public static void showSpotlight(String tvText, String subheadingTvText, View view, Activity activity, String usageId){
        new SpotlightView.Builder(activity)
                .introAnimationDuration(400)
                .performClick(true)
                .fadeinTextDuration(400)
                .headingTvColor(Color.parseColor("#eb273f"))
                .headingTvSize(32)
                .headingTvText(tvText)
                .subHeadingTvColor(Color.parseColor("#ffffff"))
                .subHeadingTvSize(14)
                .subHeadingTvText(subheadingTvText)
                .maskColor(Color.parseColor("#dc000000"))
                .target(view)
                .lineAnimDuration(400)
                .lineAndArcColor(Color.parseColor("#eb273f"))
                .dismissOnTouch(true)
                .dismissOnBackPress(true)
                .enableDismissAfterShown(true)
                .usageId(usageId) //UNIQUE ID
                .show();
    }

    /**
     * Checks if user has user id saved before.
     * @param context
     * @return
     */
    public static boolean isFirstTimeInApp(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore&&context.equals(TopicDetailActivity.class)) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    /**
     * Helper method for converting JSON input stream to string.
     * @param is
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            str = sb.toString();
        }
        return str;
    }


    public static boolean networkIsAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
