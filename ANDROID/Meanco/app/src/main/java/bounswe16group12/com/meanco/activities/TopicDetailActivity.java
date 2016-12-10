package bounswe16group12.com.meanco.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;
import java.util.Random;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.adapters.CustomTopicDetailAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.PostComment;

public class TopicDetailActivity extends AppCompatActivity {
    Topic topic;
    String title;
    public static CustomTopicDetailAdapter adapter;
    public static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        title = getIntent().getStringExtra("activityTitle").toString();
        setTitle(title);
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        int topicId = db.getTopicId(title);
        topic = db.getTopic(topicId);

        FloatingActionButton comment_fab = (FloatingActionButton) findViewById(R.id.fabComment);
        comment_fab.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final View customView = getLayoutInflater().inflate(R.layout.edit_comment, null, false);
                        final EditText content = (EditText) customView.findViewById(R.id.edit_comment_edittext);

                        new AlertDialog.Builder(TopicDetailActivity.this)
                                .setTitle("Add Comment")
                                .setView(customView)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: Will get id from HTTP:POST
                                        Comment c = new Comment(-1,topic.topicId,content.getText().toString());
                                        new PostComment(MeancoApplication.POST_COMMENT_URL,c,MeancoApplication.userId,TopicDetailActivity.this).execute();
                                     //   DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                    //    databaseHelper.addComment(c);

                                      //  TopicDetailActivityFragment.mCommentsAdapter.add(c.content);
                                      //  TopicDetailActivityFragment.mCommentsAdapter.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        List<Relation> relations = databaseHelper.getAllRelations(topic.topicId);
        adapter=new CustomTopicDetailAdapter(getApplicationContext(), R.layout.relation_dialog_view, relations, topic.topicId);

        final View customView = getLayoutInflater().inflate(R.layout.relation_dialog_view, null, false);


        listView = (ListView) customView.findViewById(R.id.relations_list);
        listView.setAdapter(adapter);



        if (id == R.id.action_relation) {
            new AlertDialog.Builder(TopicDetailActivity.this)
                    .setTitle(title + "'s Relations")
                    .setView(customView)
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
