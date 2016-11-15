package bounswe16group12.com.meanco.objects;

import java.util.ArrayList;

/**
 * Created by feper on 11/9/2016.
 */

public class Topic {
    private String topicName;
    private ArrayList<Tag> tags;

    public Topic (String topicName, ArrayList<Tag> tags){
        this.topicName = topicName;
        this.tags = tags;
    }

    public String getTopicName() {
        return topicName;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
