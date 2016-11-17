package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Comment {

    public int topic_id;
    public String content;

    public Comment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
