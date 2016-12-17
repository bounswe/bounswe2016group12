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

    @Override
    public String toString() {
        return "Relation{" +
                "relationId=" + relationId +
                ", isBidirectional=" + isBidirectional +
                ", topicFrom=" + topicFrom +
                ", topicTo=" + topicTo +
                ", relationName='" + relationName + '\'' +
                '}';
    }

    public Relation(){} //Empty constructor to define

    /**
     *
     * @param relationId Unique relation id which will be received by post request.
     * @param relationName Label of the relation.
     * @param topicFrom Starting point of arrow.
     * @param topicTo Endpoint of the arrow.
     * @param isBidirectional If arrow is bidirectional, topicFrom <-->topicTo. Else, topicFrom --> topicTo
     */
    public Relation(int relationId,String relationName, int topicFrom, int topicTo, boolean isBidirectional) {
        this.isBidirectional = isBidirectional;
        this.topicFrom = topicFrom;
        this.topicTo = topicTo;
        this.relationName = relationName;
        this.relationId = relationId;
    }
}
