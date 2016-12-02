package bounswe16group12.com.meanco.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Topic;
import me.originqiu.library.EditTag;
import me.originqiu.library.MEditText;


public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    static ArrayList<String> tagsOfTopic; //tags that are bound to topics
    SearchView searchView;

    //Home activity has search functionality, so changing the default menu is needed.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

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
            public boolean onMenuItemActionExpand(MenuItem item) {return true;}
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Add relation floating action button
        final FloatingActionButton relation_fab = (FloatingActionButton) findViewById(R.id.add_relation);
        relation_fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tagsOfTopic = new ArrayList<>();
                        final View customView = getLayoutInflater().inflate(R.layout.edit_relation, null, false);
                        final EditText topicName = (EditText) customView.findViewById(R.id.topic_name);
                        final EditText relationName = (EditText) customView.findViewById(R.id.relation_name);
                        final EditText topicName2 = (EditText) customView.findViewById(R.id.topic_name_2);
                        final CheckBox bidirectional = (CheckBox) customView.findViewById(R.id.bidirectional);

                        //Open alert dialog when button is pressed.
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Add relation")
                                .setView(customView)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Topic topicFrom = null;
                                        Topic topicTo = null;
                                        int count = 0;
                                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                        List<Topic> topics = databaseHelper.getAllTopics();

                                        for (Topic topic : topics) {
                                            if (count == 2) break; //both exist in db
                                            if (topic.getTopicName().equals(topicName.getText().toString())) {
                                                topicFrom = topic;
                                                count++;
                                            } else if (topic.getTopicName().equals(topicName2.getText().toString())) {
                                                topicTo = topic;
                                                count++;
                                            }
                                        }
                                        if (topicFrom != null && topicTo != null) {

                                            String rltName = relationName.getText().toString();

                                            Relation rel = new Relation(rltName, topicFrom.topicName, topicTo.topicName, bidirectional.isEnabled());
                                            databaseHelper.addOrUpdateRelation(rel);
                                        }

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .show();
                    }

                }
        );

        //ADD TOPIC

        final FloatingActionButton topic_fab = (FloatingActionButton) findViewById(R.id.add_topic);
        topic_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tagsOfTopic = new ArrayList<>();

                final View customView = getLayoutInflater().inflate(R.layout.customview_alerttopic, null, false);
                final EditTag editTagView = (EditTag) customView.findViewById(R.id.edit_tag_view);
                final MEditText mEditText = (MEditText) customView.findViewById(R.id.meditText);
                final EditText topicName = (EditText) customView.findViewById(R.id.topic_name);
                final EditText relationName = (EditText) customView.findViewById(R.id.relation_name);
                final EditText topicName2 = (EditText) customView.findViewById(R.id.topic_name_2);
                final CheckBox bidirectional = (CheckBox) customView.findViewById(R.id.bidirectional);


                mEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence returnedResult, int start,
                                              int before, int count) {

                        String result = returnedResult.toString();
                        if (result.length() == 0) return;
                        if (result.charAt(result.length() - 1) == ('\n')) {
                            String s = result.substring(0, result.length() - 1);
                            editTagView.addTag(s);
                            mEditText.setText("");
                            tagsOfTopic.add(s);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                });
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Add topic")
                        .setView(customView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                List<Topic> topics = databaseHelper.getAllTopics();

                                Topic topicFrom = new Topic(topicName.getText().toString(), tagsOfTopic);
                                Topic topicTo = new Topic(topicName2.getText().toString(), tagsOfTopic);
                                String rltName = relationName.getText().toString();

                                if (topicFrom.topicName.equals("")) {
                                    Toast.makeText(HomeActivity.this, "PLEASE ENTER A TOPIC NAME.", Toast.LENGTH_LONG).show();
                                } else if (topicTo.topicName.equals("") && rltName.equals("")) {
                                    boolean isFound = false;
                                    for (Topic topic : topics) {
                                        if (topic.topicName.equals(topicFrom.topicName)) {
                                            Toast.makeText(HomeActivity.this, "TOPIC NAME CREATED BEFORE.", Toast.LENGTH_LONG).show();
                                            isFound = true;
                                            break;
                                        }
                                    }
                                    if (!isFound) {
                                        databaseHelper.addOrUpdateTopic(topicFrom);
                                    }

                                } else if (topicTo.topicName.equals("") || rltName.equals("")) {
                                    boolean isFound = false;
                                    for (Topic topic : topics) {
                                        if (topic.topicName.equals(topicFrom.topicName)) {
                                            Toast.makeText(HomeActivity.this, "TOPIC NAME CREATED BEFORE.", Toast.LENGTH_LONG).show();
                                            isFound = true;
                                            break;
                                        }
                                    }
                                    if (!isFound) {
                                        databaseHelper.addOrUpdateTopic(topicFrom);
                                        Toast.makeText(HomeActivity.this, "TOPIC ADDED BUT RELATION NOT (MISSING FIELDS).", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    boolean isFound = false;
                                    for (Topic topic : topics) {
                                        if (topic.topicName.equals(topicFrom.topicName)) {
                                            Toast.makeText(HomeActivity.this, "TOPIC NAME CREATED BEFORE.", Toast.LENGTH_LONG).show();
                                            isFound = true;
                                            break;
                                        }
                                    }
                                    if (!isFound) {
                                        databaseHelper.addOrUpdateTopic(topicFrom);
                                        Toast.makeText(HomeActivity.this, "TOPIC ADDED.", Toast.LENGTH_SHORT).show();
                                        Relation rel = new Relation(rltName, topicFrom.topicName, topicTo.topicName, bidirectional.isEnabled());
                                        databaseHelper.addOrUpdateRelation(rel);
                                        Toast.makeText(HomeActivity.this, "RELATION ADDED.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                HomeActivityFragment.adapter.add(topicFrom);
                                HomeActivityFragment.adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();


            }

        });


        final FloatingActionButton tag_fab = (FloatingActionButton) findViewById(R.id.add_tag);
        tag_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsOfTopic = new ArrayList<>();
                View input = getLayoutInflater().inflate(R.layout.edit_tag, null, false);

                final EditTag editTagView = (EditTag) input.findViewById(R.id.edit_tag_view);
                final MEditText mEditText = (MEditText) input.findViewById(R.id.meditText);
                final EditText topicName = (EditText) input.findViewById(R.id.topic_name);
                mEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence returnedResult, int start,
                                              int before, int count) {

                        String result = returnedResult.toString();
                        if (result.length() == 0) return;
                        if (result.charAt(result.length() - 1) == ('\n')) {
                            String s = result.substring(0, result.length() - 1);
                            editTagView.addTag(s);
                            mEditText.setText("");
                            tagsOfTopic.add(s);

                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Add tags")
                        .setView(input)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Topic foundTopic = null;
                                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                List<Topic> topics = databaseHelper.getAllTopics();
                                for (Topic t : topics) {
                                    if (t.getTopicName().equals(topicName.getText().toString())) {
                                        t.getTags().addAll(tagsOfTopic);
                                        foundTopic = t;
                                        break;
                                    }
                                }
                                if (foundTopic != null) {
                                    databaseHelper.addOrUpdateTopic(foundTopic);
                                    HomeActivityFragment.adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog", "Negative");
                            }
                        })
                        .show();
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
        if (TextUtils.isEmpty(newText)) {
            HomeActivityFragment.adapter.getFilter().filter("");
            HomeActivityFragment.listView.clearTextFilter();

        } else {
            HomeActivityFragment.adapter.getFilter().filter(newText.toString());


        }
        return true;
    }

}
