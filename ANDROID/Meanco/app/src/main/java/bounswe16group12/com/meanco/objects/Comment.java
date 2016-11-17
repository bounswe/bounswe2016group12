package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Comment {

    public String topicName;
    public String content;

    public Comment(){}

    public Comment(String topicName, String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
