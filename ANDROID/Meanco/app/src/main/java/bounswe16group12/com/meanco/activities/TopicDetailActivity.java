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
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;
import java.util.Random;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetTopicDetail;

public class TopicDetailActivity extends AppCompatActivity {
    Topic topic;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        title = getIntent().getStringExtra("activityTitle").toString();
        setTitle(title);
        DatabaseHelper db = DatabaseHelper.getInstance(getApplicationContext());
        int topicId = db.getTopicId(title);
        topic = db.getTopic(topicId);

        new GetTopicDetail(MeancoApplication.SITE_URL,""+topicId, getApplicationContext()).execute();

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
                                        Comment c = new Comment((new Random()).nextInt(100),topic.topicId,content.getText().toString());
                                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                        databaseHelper.addComment(c);

                                        TopicDetailActivityFragment.mCommentsAdapter.add(c.content);
                                        TopicDetailActivityFragment.mCommentsAdapter.notifyDataSetChanged();
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
        final View customView = getLayoutInflater().inflate(R.layout.relation_dialog_item, null, false);

        TextView rn = (TextView) customView.findViewById(R.id.relation_name);
        ImageView iv = (ImageView) customView.findViewById(R.id.arrow_direction_picture);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
        List<Relation> relations = databaseHelper.getAllRelations(topic.topicId);

        //TODO: implement adapter.

        for(Relation r: relations){
                rn.setText(r.relationName);
                if(!r.isBidirectional)
                    iv.setImageResource(R.drawable.right_arrow);
        }

        //dummy
        rn.setText("politician");
        iv.setImageResource(R.drawable.left_arrow);

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
