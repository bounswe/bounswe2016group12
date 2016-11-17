package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Relation {
    public boolean isBidirectional;
    public String topicFrom;
    public String topicTo;
    public String relationName;

    public Relation(){}

    public Relation(String relationName, String topicFrom, String topicTo, boolean isBidirectional) {
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

    public String getRelationName() {
        return relationName;
    }
}
