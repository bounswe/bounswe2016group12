package bounswe16group12.com.meanco;

import android.app.Application;

import bounswe16group12.com.meanco.tasks.GetTopicList;

/**
 * Created by Ezgi on 12/2/2016.
 */

public class MeancoApplication extends Application {

    public static final String SITE_URL = "http://46.101.253.73:8000/API/T/";
    public static final String POST_TAG_URL = "http://46.101.253.73:8000/API/AddTag";
    public static final String POST_TOPIC_URL = "http://46.101.253.73:8000/API/AddTopic/";
    public static final String POST_COMMENT_URL = "http://46.101.253.73:8000/API/AddComment/";
    public static final String POST_RELATION_URL = "http://46.101.253.73:8000/API/AddRelation/";
    public static final String WIKIDATA_URL = "https://www.wikidata.org/w/api.php?action=wbsearchentities&language=en&format=json&search=";

    public MeancoApplication() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

    }

}
