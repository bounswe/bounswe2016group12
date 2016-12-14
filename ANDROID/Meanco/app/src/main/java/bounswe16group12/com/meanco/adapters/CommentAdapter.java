package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.activities.TopicSearchActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.VoteComment;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by Ezgi on 12/10/2016.
 */

public class CommentAdapter extends ArrayAdapter <Comment> {
    private int topicId;
    public static List<Comment> comments;
    public CommentAdapter(Context context, int resource, int topicId) {
        super(context, resource);
        comments = DatabaseHelper.getInstance(context).getAllComments(topicId);
    }

    @Override
    public int getCount(){
        if(comments==null)
            return 0;
        else
            return comments.size();
    }

    @Override
    public Comment getItem(int position){
        return comments.get(position);
    }

    @Override
    public int getPosition(Comment item){
        return comments.indexOf(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Comment c = getItem(position);

       // LinearLayout usernameLayout = null;
        TextView contentTextView = null;
        TextView usernameTextView = null;
       // final ImageButton downvoteBtn = null;
        //final ImageButton upvoteBtn = null;

        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.comment_listitem, null);
        }

        contentTextView = (TextView) v.findViewById(R.id.comment_item);
        contentTextView.setText(c.content);
        //usernameLayout = (LinearLayout) v.findViewById(R.id.username_linearlayout);
        usernameTextView = (TextView) v.findViewById(R.id.username_item);
        usernameTextView.setText("username");

        final ImageButton downvoteBtn = (ImageButton) v.findViewById(R.id.downvote_button);
        final ImageButton upvoteBtn = (ImageButton) v.findViewById(R.id.upvote_button);

        downvoteBtn.setSelected(false);
        upvoteBtn.setSelected(false);

        downvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(downvoteBtn.isSelected()){
                    downvoteBtn.setSelected(false);
                    new VoteComment(MeancoApplication.VOTE_COMMENT_URL,getItem(position),false,getContext()).execute();
                }
                else{
                    downvoteBtn.setSelected(true);
                    upvoteBtn.setSelected(false);
                    new VoteComment(MeancoApplication.VOTE_COMMENT_URL,getItem(position),false,getContext()).execute();
                }
            }
        });

        upvoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upvoteBtn.isSelected()){
                    upvoteBtn.setSelected(false);
                    new VoteComment(MeancoApplication.VOTE_COMMENT_URL,getItem(position),true,getContext()).execute();
                }
                else{
                    upvoteBtn.setSelected(true);
                    downvoteBtn.setSelected(false);
                    new VoteComment(MeancoApplication.VOTE_COMMENT_URL,getItem(position),true,getContext()).execute();
                }
            }
        });
        
        return v;
    }
}

