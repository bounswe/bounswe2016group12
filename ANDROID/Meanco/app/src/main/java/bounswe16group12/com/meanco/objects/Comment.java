package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Comment {

    public int commentId;
    public int topicId;
    public String content;
    public String username;

    public Comment(){}//Empty constructor to define

    /**
     *
     * @param commentId Comment's unique id which will be received by a post request.
     * @param topicId The topic that the comment has been made to.
     * @param content Content of the topic.
     * @param username The user that made the comment.
     */
    public Comment(int commentId, int topicId, String content, String username) {
        this.commentId = commentId;
        this.topicId = topicId;
        this.content = content;
        this.username = username;
    }

}
