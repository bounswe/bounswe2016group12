package bounswe16group12.com.meanco.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.PostTag;
import bounswe16group12.com.meanco.tasks.PostTopic;
import me.originqiu.library.EditTag;
import me.originqiu.library.MEditText;


public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    static ArrayList<Tag> tagsOfTopic; //tags that are bound to topics
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
                        final EditText topicNameEdit = (EditText) customView.findViewById(R.id.topic_name);
                        final EditText relationNameEdit = (EditText) customView.findViewById(R.id.relation_name);
                        final EditText topicName2Edit = (EditText) customView.findViewById(R.id.topic_name_2);
                        final CheckBox bidirectionalEdit = (CheckBox) customView.findViewById(R.id.bidirectional);

                        //Open alert dialog when button is pressed.
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Add relation")
                                .setView(customView)
                                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());

                                        //TODO:Change ID.

                                        String topicName = topicNameEdit.getText().toString();
                                        String topicName2 = topicName2Edit.getText().toString();
                                        String relationName = relationNameEdit.getText().toString();
                                        boolean isBidirectional = bidirectionalEdit.isEnabled();

                                        //Relation

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

               /* final View customView = getLayoutInflater().inflate(R.layout.customview_alerttopic, null, false);
                final EditTag editTagView = (EditTag) customView.findViewById(R.id.edit_tag_view);
                final MEditText mEditText = (MEditText) customView.findViewById(R.id.meditText);
                final EditText topicNameEdit = (EditText) customView.findViewById(R.id.topic_name);
                final EditText relationNameEdit = (EditText) customView.findViewById(R.id.relation_name);
                final EditText topicName2Edit = (EditText) customView.findViewById(R.id.topic_name_2);
                final CheckBox bidirectionalEdit = (CheckBox) customView.findViewById(R.id.bidirectional);


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
                            tagsOfTopic.add(new Tag(-1, "context",s,s));
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                });*/

                EditText temp = new EditText(HomeActivity.this);
                temp.setHint("Enter topic name");
                final EditText topicNameInput = temp;


                //TODO: next goes to intent
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Add topic")
                        .setView(topicNameInput)
                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String topicName = topicNameInput.getText().toString();

                                Intent i = new Intent(HomeActivity.this, TagSearchActivity.class);
                                i.putExtra("topicName", topicName);
                                startActivity(i);


                                /*DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());

                                String topicName = topicNameEdit.getText().toString();
                                //TODO:Change ID.
                                Topic topic = new Topic(-1, topicName, tagsOfTopic);
                                //databaseHelper.addTopic(topic);

                                new PostTopic(topic,getApplicationContext()).execute();

                                String topicName2 = topicName2Edit.getText().toString();
                                String relationName = relationNameEdit.getText().toString();
                                boolean isBidirectional = bidirectionalEdit.isEnabled();

                                //Relation

                                HomeActivityFragment.adapter.add(topic);
                                HomeActivityFragment.adapter.notifyDataSetChanged();*/
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
                            tagsOfTopic.add(new Tag(-1, "context",s,s));

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
                                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                for(Tag t: tagsOfTopic) {
                                    databaseHelper.addTag(t);
                                    new PostTag(MeancoApplication.POST_TAG_URL, getApplicationContext(), t);
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
