package bounswe16group12.com.meanco.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.adapters.TagSearchAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetWikiData;
import bounswe16group12.com.meanco.tasks.PostTopic;

public class TagSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    String title;
    SearchView searchView;
    ListView listView;
    public static ArrayList<Tag> checkedTags = new ArrayList<>();
    public static TagSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_search);

        final String topicName = getIntent().getStringExtra("topicName").toString();
        title = "Add topic - " + topicName;
        setTitle(title);


        listView = (ListView) findViewById(R.id.tag_search_listview);
        adapter = new TagSearchAdapter(TagSearchActivity.this, R.layout.activity_tag_search);
        listView.setAdapter(adapter);

        Button addTopicButton = (Button) findViewById(R.id.add_topic_button);
        addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TagSearchActivity.this, HomeActivity.class);
                TagSearchAdapter.wikiTags.clear();
                adapter.clear();
                Topic topic = new Topic(-1, topicName, checkedTags);
                new PostTopic(topic, TagSearchActivity.this).execute();
                checkedTags.clear();
                startActivity(i);
                finish();

            }
        });

        //TODO: click on item, get
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("Search tag...");

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

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            ;
        } else {
            if(newText.length()>1){
                new GetWikiData(MeancoApplication.WIKIDATA_URL+newText, TagSearchActivity.this).execute();
            }

        }
        return true;
    }
}