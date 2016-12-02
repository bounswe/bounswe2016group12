package bounswe16group12.com.meanco.objects;

import java.util.ArrayList;

/**
 * Created by feper on 11/9/2016.
 */

public class Topic {
    public int topicId;
    public String topicName;
    public ArrayList<Tag> tags;
    //public ArrayList<Comment> comments;

    public Topic(){

    }

    public Topic (int topicId, String topicName, ArrayList<Tag> tags){
        this.topicId = topicId;
        this.topicName = topicName;
        this.tags = tags;
    }

}
