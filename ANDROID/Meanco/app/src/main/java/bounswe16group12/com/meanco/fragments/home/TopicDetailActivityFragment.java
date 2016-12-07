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
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
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
            TextView tagView = CustomHomeAdapter.beautifyTagView(text, getContext());
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
}
