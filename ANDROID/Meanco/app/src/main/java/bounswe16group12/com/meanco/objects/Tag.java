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

    public Tag(int tagId,String context,String tagName,String URL){
        this.tagName = tagName;
        this.context = context;
        this.tagId=tagId;
        this.URL = URL;
    }
}
