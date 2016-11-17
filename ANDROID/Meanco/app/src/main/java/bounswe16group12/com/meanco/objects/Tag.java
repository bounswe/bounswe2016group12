package bounswe16group12.com.meanco.objects;

/**
 * Created by feper on 11/9/2016.
 */

public class Tag {
    public int tagId;
    public String tagName;

    public Tag(String tagName){
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
