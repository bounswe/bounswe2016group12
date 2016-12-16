package bounswe16group12.com.meanco.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.tasks.GetTopicList;
import bounswe16group12.com.meanco.utils.Functions;


public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    static ArrayList<Tag> tagsOfTopic; //tags that are bound to topics
    SearchView searchView;
    private Tracker mTracker;

    //Home activity has search functionality, so changing the default menu is needed.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        mTracker = ((MeancoApplication) getApplication()).getDefaultTracker();
        mTracker.setScreenName("HOME_ACTIVITY");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        String logStringOnMenu = "Log";
        if(Functions.getUserId(HomeActivity.this) == -1)
            logStringOnMenu+="in";
        else {

            menu.add("Profile").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
            logStringOnMenu += "out";

        }



        menu.add(logStringOnMenu).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Functions.clearUserPreferences(getApplicationContext());
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                return true;
            }
        });



        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
            searchView.setIconifiedByDefault(false);
            searchView.setQueryHint("Search topic...");

        }



        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchView.clearFocus(); //to close soft keyboard when collapsed


                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {



                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.rsz_meanco_logo);
        setTitle("  Meanco");

        if(Functions.isFirstTimeInApp(HomeActivity.this)){

            Functions.showSpotlight("See", "Long press on a topic to see its relations.",
                    findViewById(R.id.content_home), this, "Relations");

        }



        //Add relation floating action button
        final FloatingActionButton relation_fab = (FloatingActionButton) findViewById(R.id.add_relation);
        relation_fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Functions.getUserId(HomeActivity.this) == -1) {

                            Functions.showNotLoggedInAlert(HomeActivity.this);

                        } else {
                            tagsOfTopic = new ArrayList<>();
                            final View customView = getLayoutInflater().inflate(R.layout.edit_relation, null, false);
                            final EditText relationNameEdit = (EditText) customView.findViewById(R.id.relation_name);
                            final CheckBox bidirectionalEdit = (CheckBox) customView.findViewById(R.id.bidirectional);

                            //Open alert dialog when button is pressed.
                            final AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("Add relation")
                                    .setView(customView)
                                    .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            String relationName = relationNameEdit.getText().toString();
                                            boolean isBidirectional = bidirectionalEdit.isChecked();

                                            Intent i = new Intent(HomeActivity.this, TopicSearchActivity.class);
                                            i.putExtra("relationName", relationName);
                                            i.putExtra("isBidirectional", isBidirectional);
                                            i.putExtra("fromOrTo", "from");
                                            startActivity(i);


                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }).show();

                        }

                    }
                }
        );

        //ADD TOPIC

        final FloatingActionButton topic_fab = (FloatingActionButton) findViewById(R.id.add_topic);
        topic_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Functions.getUserId(HomeActivity.this) == -1) {

                    Functions.showNotLoggedInAlert(HomeActivity.this);

                } else {

                    EditText temp = new EditText(HomeActivity.this);
                    temp.setHint("Enter topic name");
                    final EditText topicNameInput = temp;


                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Add topic")
                            .setView(topicNameInput)
                            .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String topicName = topicNameInput.getText().toString();

                                    Intent i = new Intent(HomeActivity.this, TagSearchActivity.class);
                                    i.putExtra("topicName", topicName);
                                    i.putExtra("ifDetail", "false");
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();


                }
            }

        });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return Functions.filterData(newText, HomeActivityFragment.adapter, HomeActivityFragment.adapter.filteredData, getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        //Blocks  return action of back button to prevent user go back to login page.
    }
}
