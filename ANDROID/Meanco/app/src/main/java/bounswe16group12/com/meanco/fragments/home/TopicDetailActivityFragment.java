package bounswe16group12.com.meanco.fragments.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.activities.TagSearchActivity;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.adapters.CommentAdapter;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.EditComment;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopicDetailActivityFragment extends Fragment {
    public static CommentAdapter mCommentsAdapter;
    public static ArrayAdapter<SpannableStringBuilder> mTagsAdapter;
    private Tracker mTracker;

    public TopicDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**
         * Google data analytics.
         */
        mTracker = ((MeancoApplication) getActivity().getApplication()).getDefaultTracker();
        mTracker.setScreenName("TOPIC_DETAIL_FRAGMENT");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

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
/**
 * Populate tag list view.
 */
        mTagsAdapter = new ArrayAdapter<>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_detail_tag, // The name of the layout ID.
                R.id.list_item_detail_tag_textview, // The ID of the textview to populate.
                texts
        );


        ListView tagListView = (ListView) rootView.findViewById(R.id.listView_tags);
        tagListView.setAdapter(mTagsAdapter);

        /**
         * Populate comment list view.
         */
        mCommentsAdapter = new CommentAdapter(
                getActivity(), // The current context (this activity)
                R.layout.comment_listitem, // The name of the layout ID.
                topicId);

        ListView listView = (ListView) rootView.findViewById(R.id.listView_topic_comments);
        listView.setAdapter(mCommentsAdapter);

        /**
         * Edit comment on long click.
         */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Comment c = mCommentsAdapter.getItem(position);

                EditText temp = new EditText(getContext());
                temp.setText(c.content);
                final EditText commentEditInput = temp;

                if(c.username.equals(Functions.getUsername(getContext()))){
                    new AlertDialog.Builder(getContext())
                            .setTitle("Edit Comment")
                            .setView(commentEditInput)
                            .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    c.content = commentEditInput.getText().toString();

                                    new EditComment(MeancoApplication.EDIT_COMMENT_URL,c,getContext()).execute();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    /**
     * Update adapters whenever data is changed.
     * @param databaseHelper
     * @param topicId
     */
    public static void updateAdapters(DatabaseHelper databaseHelper, int topicId){
        mCommentsAdapter.clear();
        mCommentsAdapter.comments.clear();
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
