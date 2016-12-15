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
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.CustomTopicDetailAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.PostComment;
import bounswe16group12.com.meanco.utils.Functions;

public class TopicDetailActivity extends AppCompatActivity {
    Topic topic;
    public static CustomTopicDetailAdapter adapter;
    public static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        int topicId = getIntent().getIntExtra("topicId",-1);
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        topic = db.getTopic(topicId);
        setTitle(topic.topicName);

        if(Functions.isFirstTimeInApp(TopicDetailActivity.this)){

            Functions.showSpotlight("Edit", "Long press on a comment to edit.",
                    findViewById(R.id.listView_topic_comments), this, "Comment");

        }



        FloatingActionButton comment_fab = (FloatingActionButton) findViewById(R.id.fabComment);
        comment_fab.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

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

                                            Comment c = new Comment(-1, topic.topicId, content.getText().toString());
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
                    .setTitle(topic.topicName + "'s Relations")
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
