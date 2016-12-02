package bounswe16group12.com.meanco;

import android.app.Application;

import bounswe16group12.com.meanco.tasks.GetTopicList;

/**
 * Created by Ezgi on 12/2/2016.
 */

public class MeancoApplication extends Application {

    public static String SITE_URL = "http://46.101.253.73:8000/API/T/";

    public MeancoApplication() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

    }

    @Override
    public void onCreate() {
        super.onCreate();

        new GetTopicList(SITE_URL, getApplicationContext()).execute();


    }
}
