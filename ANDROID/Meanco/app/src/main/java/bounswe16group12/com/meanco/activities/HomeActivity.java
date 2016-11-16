package bounswe16group12.com.meanco.activities;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import me.originqiu.library.EditTag;
import me.originqiu.library.MEditText;



public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    static ArrayList<Tag> tagsOfTopic;
    SearchView searchView;

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
                searchView.clearFocus();
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                            Tag tag = new Tag(s);
                            tagsOfTopic.add(tag);
                        }
                    }

                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}

                });
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Add topic")
                        .setView(customView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: add topic to database
                                Topic topicFrom = new Topic(topicName.getText().toString(), tagsOfTopic);
                                HomeActivityFragment.getTopics().add(topicFrom);
                                String rltName = relationName.getText().toString();

                                //TODO: if topic is not in database, do not do this. Dummy variable.
                                Topic topicTo = new Topic(topicName2.getText().toString(), tagsOfTopic);
                                HomeActivityFragment.getRelations().add(new Relation(rltName, topicFrom, topicTo, false));


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
                View input = getLayoutInflater().inflate(R.layout.edit_tag, null, false);

                final EditTag editTagView = (EditTag) input.findViewById(R.id.edit_tag_view);
                final MEditText mEditText = (MEditText) input.findViewById(R.id.meditText);
                mEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence returnedResult, int start,
                                              int before, int count) {

                        String result = returnedResult.toString();
                        if (result.length() == 0) return;
                        if (result.charAt(result.length() - 1) == ('\n')) {
                            editTagView.addTag(result.substring(0, result.length() - 1));
                            mEditText.setText("");
                        }
                    }

                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}
                });

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Add tags")
                        .setView(input)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: Add tag to database.
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
            Log.i("Nomad", "onQueryTextChange Empty String");
            HomeActivityFragment.listView.clearTextFilter();

        } else {
            Log.i("Nomad", "onQueryTextChange " + newText.toString());
            HomeActivityFragment.adapter.getFilter().filter(newText.toString());


        }
        return true;
    }

}
