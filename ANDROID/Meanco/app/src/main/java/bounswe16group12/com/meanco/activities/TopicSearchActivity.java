package bounswe16group12.com.meanco.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.TopicSearchAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.PostRelation;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Topic search activity searches local db and then populates the listview.
 * User reaches this activity when she wants to add a relation between existing topics.
 * User can choose only one topic per activity instance.
 */
public class TopicSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    String title;
    SearchView searchView;
    ListView listView;
    public static String fromOrTo;
    public static Topic checkedTopic;

    int oldId = 0;
    int oldPosition=0;
    public static TopicSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_search);

        Tracker mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("TOPIC_SEARCH_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        /**
         * Get intent values from "Add relation" dialog.
         */
        final String relationName = getIntent().getStringExtra("relationName").toString();
        final boolean isBidirectional = getIntent().getBooleanExtra("isBidirectional", false);
        fromOrTo = getIntent().getStringExtra("fromOrTo").toString();


        listView = (ListView) findViewById(R.id.tag_search_listview);
        adapter = new TopicSearchAdapter(TopicSearchActivity.this, R.layout.activity_tag_search);
        listView.setAdapter(adapter);

        /**
         * Check if this instance of the activity is the first or not
         * (are we choosing first topic or second topic).
         */
        Button addRelationButton = (Button) findViewById(R.id.add_topic_button);
        if(fromOrTo.equals("from")){
            addRelationButton.setText("Next");

        }else{
            addRelationButton.setVisibility(View.VISIBLE);
            addRelationButton.setText("Add relation");

        }
        addRelationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * If it is the second instance, post relation with two topics, clear and close.
                 */
                if(fromOrTo.equals("to")) {
                    Relation r = new Relation(-1, relationName, TopicSearchAdapter.idFrom, TopicSearchAdapter.idTo, isBidirectional);
                    new PostRelation(MeancoApplication.POST_RELATION_URL, r, TopicSearchActivity.this).execute();

                    TopicSearchAdapter.relationTopics.clear();
                    adapter.clear();
                    Intent intent = new Intent(TopicSearchActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                /**
                 * If it is the first instance, go to next page and open this activity again with same intent
                 * extras.
                 */
                else
                {
                    Intent intent = new Intent(TopicSearchActivity.this, TopicSearchActivity.class);
                    intent.putExtra("relationName", relationName);
                    intent.putExtra("isBidirectional", isBidirectional);
                    intent.putExtra("fromOrTo", "to");
                    startActivity(intent);
                   // finish();
                }
            }
        });

        /**
         * Clear everything and go back if user cancels.
         */
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

            cancelButton.setText("Cancel");
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TopicSearchAdapter.relationTopics.clear();
                    adapter.clear();
                    finish();

                }
            });

        /**
         * Set background color to green when an item is selected.
         * If user decides to choose another topic, new item is colored
         * and previously colored item has transparent background again.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int newId = adapter.getItem(i).topicId;
                if(oldId!=newId){
                    listView.getChildAt(oldPosition).setBackgroundColor(Color.TRANSPARENT);
                    if(fromOrTo.equals("from")) {
                        TopicSearchAdapter.idFrom = newId;
                    }else {
                        TopicSearchAdapter.idTo = newId;
                    }
                    oldId=newId;
                    oldPosition=i;
                }
                view.setBackgroundColor(ContextCompat.getColor(TopicSearchActivity.this, R.color.colorSelectedTopic));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
            searchView.setIconifiedByDefault(false);
            if(fromOrTo.equals("from"))
                searchView.setQueryHint("Search topic from...");
            else
                searchView.setQueryHint("Search topic to...");
        }


        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.clearFocus(); //to close soft keyboard when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {return true;}
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        return true;
    }

    /**
     * On every keyboard input, topics are filtered according to query.
     * @param newText
     * @return true
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return Functions.filterData(newText.toLowerCase(), adapter, adapter.relationTopics, getApplicationContext());
    }
}
