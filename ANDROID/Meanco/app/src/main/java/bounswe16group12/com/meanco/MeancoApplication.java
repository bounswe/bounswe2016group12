package bounswe16group12.com.meanco;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.tasks.GetTopicList;

/**
 * Contains all constant URLs of the application.
 * Created by Ezgi on 12/2/2016.
 */

public class MeancoApplication extends Application {
    
    private static final String BASE_URL = "http://46.101.253.73:8000/API/";
    
    private Tracker mTracker;
    public static final String SITE_URL = BASE_URL + "T/";
    public static final String POST_TAG_URL = BASE_URL + "AddTag";
    public static final String POST_TOPIC_URL = BASE_URL + "AddTopic";
    public static final String POST_COMMENT_URL = BASE_URL + "AddComment";
    public static final String POST_RELATION_URL = BASE_URL + "AddRelation";
    public static final String WIKIDATA_URL = "https://www.wikidata.org/w/api.php?action=wbsearchentities&language=en&format=json&search=";
    public static final String SEARCH_URL = BASE_URL + "SearchTopic?search=";
    public static final String REGISTER_URL = BASE_URL + "Register";
    public static final String LOGIN_URL = BASE_URL + "Login";
    public static final String VOTE_COMMENT_URL = BASE_URL + "RateComment";
    public static final String EDIT_COMMENT_URL =BASE_URL + "EditComment";
    public static final String FOLLOW_TOPIC_URL = BASE_URL + "FollowTopic";
    public static final String GET_COMMENT_VOTES_URL = BASE_URL + "GetCommentVoters";
    public static final String GET_USER_COMMENTS_URL = BASE_URL + "GetCommentedTopic";
    public static final String GET_FOLLOWED_TOPICS_URL = BASE_URL + "GetFollowedTopic";

    public static List<Integer> followedTopicList = new ArrayList<>();

    private static final String GOOGLE_ANALYTICS_KEY = "UA-39760660-5";


    public MeancoApplication() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(GOOGLE_ANALYTICS_KEY);
        }
        return mTracker;
    }

}
