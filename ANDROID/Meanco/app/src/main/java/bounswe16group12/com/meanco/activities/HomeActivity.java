package bounswe16group12.com.meanco.activities;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import me.originqiu.library.EditTag;
import me.originqiu.library.MEditText;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final ArrayList <Tag> tagsOfTopic = new ArrayList<>();

        final FloatingActionButton topic_fab = (FloatingActionButton) findViewById(R.id.add_topic) ;
        topic_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View input = getLayoutInflater().inflate(R.layout.customview_alerttopic, null, false);
                final EditTag editTagView = (EditTag) input.findViewById(R.id.edit_tag_view);
                final MEditText mEditText = (MEditText) input.findViewById(R.id.meditText);
                mEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence returnedResult, int start,
                                              int before, int count) {

                        String result = returnedResult.toString();
                        if(result.length() == 0) return;
                        if(result.charAt(result.length() - 1)==('\n'))
                        {
                            String s = result.substring(0, result.length() - 1);
                            editTagView.addTag(s);
                           // HomeActivityFragment.setTopics(HomeActivityFragment.getTopics().add(tp););
                            mEditText.setText("");
                            Tag tag = new Tag(s);
                            tagsOfTopic.add(tag);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                new AlertDialog.Builder( HomeActivity.this )
                        .setTitle( "Add topic" )
                        .setView(input)
                        .setPositiveButton( "Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO: add to database
                                //input.getText();
                            }
                        })
                        .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        } )
                        .show();


            }

        });




        final FloatingActionButton tag_fab = (FloatingActionButton) findViewById(R.id.add_tag) ;
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
                        if(result.length() == 0) return;
                        if(result.charAt(result.length() - 1)==('\n'))
                        {
                            editTagView.addTag(result.substring(0, result.length() - 1));
                            mEditText.setText("");
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                new AlertDialog.Builder( HomeActivity.this )
                        .setTitle( "Add tags" )
                        .setView(input)
                        .setPositiveButton( "Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Negative" );
                            }
                        } )
                        .show();
            }

        });

    }

}
