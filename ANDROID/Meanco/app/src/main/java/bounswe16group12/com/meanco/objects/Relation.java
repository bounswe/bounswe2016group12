package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Relation {
    public boolean isBidirectional;
    public Topic topicFrom;
    public Topic topicTo;
    public String relationName;

    public Relation(String relationName, Topic topicFrom, Topic topicTo, boolean isBidirectional) {
        this.isBidirectional = isBidirectional;
        this.topicFrom = topicFrom;
        this.topicTo = topicTo;
        this.relationName = relationName;
    }

    public boolean isBidirectional() {
        return isBidirectional;
    }

    public void setBidirectional(boolean bidirectional) {
        isBidirectional = bidirectional;
    }

    public Topic getTopicFrom() {
        return topicFrom;
    }

    public void setTopicFrom(Topic topicFrom) {
        this.topicFrom = topicFrom;
    }

    public Topic getTopicTo() {
        return topicTo;
    }

    public void setTopicTo(Topic topicTo) {
        this.topicTo = topicTo;
    }

    public String getRelationName() {
        return relationName;
    }
}
