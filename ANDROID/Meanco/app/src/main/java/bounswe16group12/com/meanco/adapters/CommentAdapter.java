package bounswe16group12.com.meanco.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.R;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.objects.Vote;
import bounswe16group12.com.meanco.tasks.VoteComment;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * An adapter which populates comments list view on topic detail page.
 * Created by Ezgi on 12/10/2016.
 */

public class CommentAdapter extends ArrayAdapter <Comment> {
    public static List<Comment> comments;
    private int topicId;
    public CommentAdapter(Context context, int resource, int topicId) {
        super(context, resource);
        this.topicId = topicId;
        if(topicId == -100)
            comments = DatabaseHelper.getInstance(context).getComments(Functions.getUsername(context));
         else
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
        final Comment c = getItem(position);

        TextView contentTextView;
        TextView usernameTextView;
        final TextView voteCountTextView;

        if(v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.comment_listitem, null);
        }

        contentTextView = (TextView) v.findViewById(R.id.comment_item);
        contentTextView.setText(c.content);

        voteCountTextView = (TextView) v.findViewById(R.id.textView_voteCount);
        voteCountTextView.setText(Integer.toString(c.voteCount));

        final ImageButton downvoteBtn = (ImageButton) v.findViewById(R.id.downvote_button);
        final ImageButton upvoteBtn = (ImageButton) v.findViewById(R.id.upvote_button);

        usernameTextView = (TextView) v.findViewById(R.id.username_item);

        if(topicId == -100){
            Topic t = DatabaseHelper.getInstance(getContext()).getTopic(c.topicId);
            usernameTextView.setText(t.topicName);
            downvoteBtn.setVisibility(View.INVISIBLE);
            upvoteBtn.setVisibility(View.INVISIBLE);
        }
        else if(c.username.equals(Functions.getUsername(getContext()))){
            usernameTextView.setText(c.username);
            downvoteBtn.setVisibility(View.INVISIBLE);
            upvoteBtn.setVisibility(View.INVISIBLE);
        }
        else {
            usernameTextView.setText(c.username);

            downvoteBtn.setVisibility(View.VISIBLE);
            upvoteBtn.setVisibility(View.VISIBLE);

            downvoteBtn.setSelected(false);
            upvoteBtn.setSelected(false);

            if(DatabaseHelper.getInstance(getContext()).getVote(c.commentId) != null){
                Vote vote = DatabaseHelper.getInstance(getContext()).getVote(c.commentId);
                if(vote.isUpvoted){
                    upvoteBtn.setSelected(true);
                }
                else if(vote.isDownvoted){
                    downvoteBtn.setSelected(true);
                }
            }

            downvoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Functions.getUserId(getContext()) == -1) {
                        Functions.showNotLoggedInAlert(getContext());
                    }else {

                        if (downvoteBtn.isSelected()) {
                            downvoteBtn.setSelected(false);
                            new VoteComment(MeancoApplication.VOTE_COMMENT_URL, getItem(position), false, getContext()).execute();
                            voteCountTextView.setText(Integer.toString(c.voteCount+=1));
                        } else {
                            voteCountTextView.setText(Integer.toString(c.voteCount -= (upvoteBtn.isSelected() ? 2:1)));
                            downvoteBtn.setSelected(true);
                            upvoteBtn.setSelected(false);
                            new VoteComment(MeancoApplication.VOTE_COMMENT_URL, getItem(position), false, getContext()).execute();
                        }
                    }
                }
            });

            upvoteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Functions.getUserId(getContext()) == -1) {
                        Functions.showNotLoggedInAlert(getContext());
                    }else {
                        if (upvoteBtn.isSelected()) {
                            upvoteBtn.setSelected(false);
                            new VoteComment(MeancoApplication.VOTE_COMMENT_URL, getItem(position), true, getContext()).execute();
                            voteCountTextView.setText(Integer.toString(c.voteCount-=1));
                        } else {
                            voteCountTextView.setText(Integer.toString(c.voteCount += (downvoteBtn.isSelected() ? 2:1)));
                            upvoteBtn.setSelected(true);
                            downvoteBtn.setSelected(false);
                            new VoteComment(MeancoApplication.VOTE_COMMENT_URL, getItem(position), true, getContext()).execute();
                        }
                    }
                }
            });

        }
        
        return v;
    }
}


