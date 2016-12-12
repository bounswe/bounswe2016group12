package bounswe16group12.com.meanco.objects;

import java.util.ArrayList;

/**
 * Created by feper on 11/9/2016.
 */

public class Topic {
    public int topicId;
    public String topicName;

    @Override
    public String toString() {

        return "Topic{" +
                "topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                ", tags=" + tags +
                '}';
    }

    public ArrayList<Tag> tags;
    //public String context; //TODO: ADD TO EVERYWHERE
    //public ArrayList<Comment> comments;

    public Topic(){}//Empty constructor to define

    public Topic (int topicId, String topicName, ArrayList<Tag> tags){
        this.topicId = topicId;
        this.topicName = topicName;
        this.tags = tags;
    }

}
