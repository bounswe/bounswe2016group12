package bounswe16group12.com.meanco.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.TagSearchAdapter;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetWikiData;
import bounswe16group12.com.meanco.tasks.PostTag;
import bounswe16group12.com.meanco.tasks.PostTopic;

/**
 * Tag search activity gets search results from WikiData by sending get requests to wikidata.org
 * and then populated on a listview.
 * User reaches this activity when she wants to add a topic or add tag(s) to existing topic.
 * User can choose multiple tags to add to a topic.
 */

public class TagSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    SearchView searchView;
    ListView listView;

    public static ArrayList<Tag> checkedTags = new ArrayList<>();

    public static TagSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Check where the intent comes from.
         * Two options:
         * "true" = intent comes from topic detail.
         * "false" = intent comes from add topic (home page alert dialog).
         */
        final String intentFromDetail = getIntent().getStringExtra("ifDetail");

        /**
         * Google analytics data.
         */
        Tracker mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("TAG_SEARCH_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAutoActivityTracking(true);

        setContentView(R.layout.activity_tag_search);


        final String topicName = getIntent().getStringExtra("topicName").toString();


        listView = (ListView) findViewById(R.id.tag_search_listview);
        adapter = new TagSearchAdapter(TagSearchActivity.this, R.layout.activity_tag_search);
        listView.setAdapter(adapter);

        Button addTopicButton = (Button) findViewById(R.id.add_topic_button);
        addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * User adds new topic to system, post topic to db.
                 */
                if(intentFromDetail.equals("false")) {
                    Topic topic = new Topic(-1, topicName, checkedTags);
                    new PostTopic(topic, TagSearchActivity.this,MeancoApplication.POST_TOPIC_URL).execute();
                }

                /**
                 * User adds new tags to existing topics, post tags (with topic id) to db.
                 */
                else
                {
                    boolean isLast = false;
                    int index = 0;
                    for(Tag t: checkedTags){
                        if(index == checkedTags.size()-1) isLast=true;

                        index++;
                        new PostTag(MeancoApplication.POST_TAG_URL, t, getIntent().getIntExtra("topicId", -1), isLast, TagSearchActivity.this)
                                .execute();

                    }
                }
                //clears the checked array in post topic
                TagSearchAdapter.wikiTags.clear();
                adapter.clear();
                finish();

            }
        });

        /**
         * Clear everything and go back to the activity that gave intent.
         */
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkedTags.clear();
                TagSearchAdapter.wikiTags.clear();
                adapter.clear();
                finish();

            }
        });

        /**
         * Toggle checkbox and remove/add tag from/to checked array.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView box = (CheckedTextView) view.findViewById(R.id.checkedTextView);
                if(box.isChecked()) {
                    box.setChecked(false);
                    checkedTags.remove(adapter.getItem(i));
                }
                else{
                    box.setChecked(true);
                    checkedTags.add(adapter.getItem(i));
                }
            }
        });




    }

    /**
     * Search functionality for searching tags of a topic.
     * @param menu
     * @return true
     */
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
            searchView.setQueryHint("Search tag for " + getIntent().getStringExtra("topicName").toString() +"...");

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
     * Whenever user types a letter, a wikidata request is made. This enables autocomplete.
     * @param newText
     * @return true
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            new GetWikiData(MeancoApplication.WIKIDATA_URL+newText, TagSearchActivity.this).execute();
        }
        return true;
    }
}
