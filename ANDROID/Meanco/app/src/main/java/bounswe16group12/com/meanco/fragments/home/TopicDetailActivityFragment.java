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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrius.accordionswipelayout.library.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.adapters.CommentAdapter;
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
    public static CommentAdapter mCommentsAdapter;
    public static ArrayAdapter<SpannableStringBuilder> mTagsAdapter;

    public TopicDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_topic_detail, container, false);

        int topicId = getActivity().getIntent().getIntExtra("topicId",-1);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getContext());

        Topic topic = databaseHelper.getTopic(topicId);

        //tags
        List<SpannableStringBuilder> texts = new ArrayList<>();

        for(Tag t: topic.tags){
            String text = t.tagName + ": " + t.context;

            texts.add(beautifyString(text));
        }

        mTagsAdapter = new ArrayAdapter<>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_detail_tag, // The name of the layout ID.
                R.id.list_item_detail_tag_textview, // The ID of the textview to populate.
                texts
        );


        ListView tagListView = (ListView) rootView.findViewById(R.id.listView_tags);
        tagListView.setAdapter(mTagsAdapter);

        mCommentsAdapter = new CommentAdapter(
                getActivity(), // The current context (this activity)
                R.layout.comment_listitem, // The name of the layout ID.
                topicId);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_topic_comments);
        listView.setAdapter(mCommentsAdapter);

        return rootView;
    }

    public static void updateAdapters(DatabaseHelper databaseHelper, int topicId){
        mCommentsAdapter.clear();
        mTagsAdapter.clear();
        List<Comment> comments = databaseHelper.getAllComments(topicId);
        List<Tag> tags = databaseHelper.getTopic(topicId).tags;
        mCommentsAdapter.comments.addAll(comments);
        for(Tag t: tags){
            String text = t.tagName + ": " + t.context;
            mTagsAdapter.add(beautifyString(text));
        }
        mCommentsAdapter.notifyDataSetChanged();
        mTagsAdapter.notifyDataSetChanged();

    }


    public static SpannableStringBuilder beautifyString(String text){

        final SpannableStringBuilder str = new SpannableStringBuilder(text);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new RelativeSizeSpan(1.25f), 0, text.indexOf(":")+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.LTGRAY), text.indexOf(":")+2, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return str;
    }
}
