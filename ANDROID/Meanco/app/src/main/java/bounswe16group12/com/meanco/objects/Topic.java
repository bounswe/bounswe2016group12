package bounswe16group12.com.meanco.objects;

import java.util.ArrayList;

/**
 * Created by feper on 11/9/2016.
 */

public class Topic {
    public int topicId;
    public String topicName;
    public ArrayList<String> tags;
    public ArrayList<Comment> comments;

    public Topic(){

    }

    public Topic (String topicName, ArrayList<String> tags){
        this.topicName = topicName;
        this.tags = tags;
    }

    public String getTopicName() {
        return topicName;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
