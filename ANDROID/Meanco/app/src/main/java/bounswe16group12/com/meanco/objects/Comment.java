package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Comment {

    public int commentId;
    public int topicId;
    public String content;

    public Comment(){}

    public Comment(int commentId, int topicId, String content) {
        this.commentId = commentId;
        this.topicId = topicId;
        this.content = content;
    }

}
