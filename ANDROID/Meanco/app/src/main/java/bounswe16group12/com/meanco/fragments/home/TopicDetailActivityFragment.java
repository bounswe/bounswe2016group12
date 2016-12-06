package bounswe16group12.com.meanco.fragments.home;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopicDetailActivityFragment extends Fragment {
    public static ArrayAdapter<String> mCommentsAdapter;

    public TopicDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        String topicName = getActivity().getIntent().getStringExtra("activityTitle").toString();

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());

        int topicId = databaseHelper.getTopicId(topicName);
        Topic topic = databaseHelper.getTopic(topicId);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout_detail);

        for (int i = 0; i < topic.tags.size(); i++) {
            String text = topic.tags.get(i).tagName + ": " + topic.tags.get(i).context;
            TextView tagView = beautifyTagView(text, getContext());
            linearLayout.addView(tagView);
        }

        List<Comment> comments = databaseHelper.getAllComments(topic.topicId);
        List<String> contents = new ArrayList<String>();

       for(Comment c : comments){
           contents.add(c.content);
       }

        mCommentsAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_detail_comment, // The name of the layout ID.
                R.id.list_item_detail_comment_textview, // The ID of the textview to populate.
                contents);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_topic_comments);
        listView.setAdapter(mCommentsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getActivity(), "Comment is Upvoted.", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public TextView beautifyTagView(String text, Context context){
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
        tagView.setPadding(15, 15, 15, 15);
        return tagView;
    }
}
