package bounswe16group12.com.meanco;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

import me.originqiu.library.EditTag;
import me.originqiu.library.MEditText;


public class HomeActivity extends AppCompatActivity {

static String text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final FloatingActionButton topic_fab = (FloatingActionButton) findViewById(R.id.add_topic) ;
        topic_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View input = getLayoutInflater().inflate(R.layout.customview_alerttopic, null, false);
                final EditTag editTagView = (EditTag) findViewById(R.id.edit_tag_view);
                final MEditText mEditText = (MEditText) editTagView.findViewById(R.id.meditText);
                mEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence returnedResult, int start,
                                              int before, int count) {

                        String result = returnedResult.toString();
                        if(result.length() == 0) return;
                        if(result.charAt(result.length() - 1)==(',')) // last character is ','
                        {
                            // comma is entered
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
                        .setTitle( "Add topic" )
                        .setView(input)
                        .setPositiveButton( "Add", new DialogInterface.OnClickListener() {
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
                View input = getLayoutInflater().inflate(R.layout.customview_alerttopic, null, false);

                new AlertDialog.Builder( HomeActivity.this )
                        .setTitle( "Add" )
                        .setView(input)
                        .setPositiveButton( "Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Positive" );
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
