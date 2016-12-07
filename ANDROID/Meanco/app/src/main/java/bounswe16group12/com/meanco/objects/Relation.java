package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Relation {
    public int relationId;
    public boolean isBidirectional;
    public int topicFrom;
    public int topicTo;
    public String relationName;

    public Relation(){} //Empty constructor to define

    public Relation(int relationId,String relationName, int topicFrom, int topicTo, boolean isBidirectional) {
        this.isBidirectional = isBidirectional;
        this.topicFrom = topicFrom;
        this.topicTo = topicTo;
        this.relationName = relationName;
        this.relationId = relationId;
    }
}
