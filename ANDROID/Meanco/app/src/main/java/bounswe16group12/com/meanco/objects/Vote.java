package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 12/16/2016.
 */

public class Vote {
    public int commentId;
    public boolean isUpvoted;
    public boolean isDownvoted;

    public Vote(){};

    public Vote(int commentId,boolean isUpvoted, boolean isDownvoted){
        this.commentId = commentId;
        this.isDownvoted = isDownvoted;
        this.isUpvoted = isUpvoted;
    }

}
