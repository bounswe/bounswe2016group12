package bounswe16group12.com.meanco.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.FollowTopic;
import bounswe16group12.com.meanco.tasks.PostComment;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Topic detail activity has comments and tags of a topic.
 * User can add a tag, a comment, vote on a comment, edit her previously added comment and follow the topic.
 */
public class TopicDetailActivity extends AppCompatActivity {
    Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        /**
         * Google analytics data.
         */
        Tracker mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("TOPIC_DETAIL_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        /**
         * Topic detail page has topic name as the title.
         */
        int topicId = getIntent().getIntExtra("topicId",-1);
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        topic = db.getTopic(topicId);
        setTitle(topic.topicName);

        /**
         * If it is user's first time in application, a spotlight that teaches the user the long press feature
         * is shown.
         */
        if(Functions.isFirstTimeInApp(TopicDetailActivity.this)){

            Functions.showSpotlight("Edit", "Long press on a comment to edit.",
                    findViewById(R.id.listView_topic_comments), this, "Comment");

        }

        /**
         * Add relation button is populated here.
         */
        FloatingActionButton comment_fab = (FloatingActionButton) findViewById(R.id.fabComment);
        comment_fab.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        /**
                         * Guests do not have right to add comment.
                         */
                        if (Functions.getUserId(TopicDetailActivity.this) == -1) {
                            Functions.showNotLoggedInAlert(TopicDetailActivity.this);

                        } else {

                            final View customView = getLayoutInflater().inflate(R.layout.edit_comment, null, false);
                            final EditText content = (EditText) customView.findViewById(R.id.edit_comment_edittext);

                            new AlertDialog.Builder(TopicDetailActivity.this)
                                    .setTitle("Add Comment")
                                    .setView(customView)
                                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            Comment c = new Comment(-1, topic.topicId, content.getText().toString(),Functions.getUsername(getApplicationContext()));
                                            new PostComment(MeancoApplication.POST_COMMENT_URL, c, getApplicationContext()).execute();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    }
                }
        );
        FloatingActionButton tag_fab = (FloatingActionButton) findViewById(R.id.fabTag);
        tag_fab.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        /**
                         * Guests do not have right to add tag.
                         */
                        if (Functions.getUserId(TopicDetailActivity.this) == -1) {
                            Functions.showNotLoggedInAlert(TopicDetailActivity.this);

                        }else {
                            Intent i = new Intent(TopicDetailActivity.this, TagSearchActivity.class);
                            i.putExtra("ifDetail", "true");
                            i.putExtra("topicName", topic.topicName);
                            i.putExtra("topicId", topic.topicId);
                            startActivity(i);
                        }
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        MenuItem item = menu.findItem(R.id.action_follow);

        if(MeancoApplication.followedTopicList.contains(topic.topicId)){
            item.setIcon(R.drawable.followed);
            item.setChecked(true);
        }
        else{
            item.setIcon(R.drawable.follow);
            item.setChecked(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         *
         * If follow topic item is selected, user follows the topic and the topic is added on her profile.
         * If it was already selected, user unfollows the topic and it is removed from her profile.
         */
        int id = item.getItemId();
        if(id==R.id.action_follow){
            new FollowTopic(MeancoApplication.FOLLOW_TOPIC_URL,topic.topicId,getApplicationContext()).execute();
            if(!item.isChecked()) {
                item.setIcon(R.drawable.followed);
                item.setChecked(true);
                Toast.makeText(getApplicationContext(), "Topic Followed.",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                item.setIcon(R.drawable.follow);
                item.setChecked(false);
                Toast.makeText(getApplicationContext(), "Topic Unfollowed.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
