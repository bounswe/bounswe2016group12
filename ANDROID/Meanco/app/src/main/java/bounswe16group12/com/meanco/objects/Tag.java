package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Tag {
    public int tagId;
    public String tagName;
    public String context;
    public String URL;

    public Tag(){}//Empty constructor to define

    /**
     *
     * @param tagId Unique tag id which will be received by post request.
     * @param context Description of the tag (ex. for Python: snake, python = tagName, snake = context
     * @param tagName Label of the tag.
     * @param URL Unique wikidata URL of the tag.
     */
    public Tag(int tagId,String context,String tagName,String URL){
        this.tagName = tagName;
        this.context = context;
        this.tagId=tagId;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", context='" + context + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
