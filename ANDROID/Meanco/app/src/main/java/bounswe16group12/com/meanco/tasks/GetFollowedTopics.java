package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by feper on 12/16/2016.
 */

public class GetFollowedTopics extends AsyncTask<Void,Void,Connect.APIResult> {

    private Context context;
    private String url;

    public GetFollowedTopics(String url, Context context){
        this.context = context;
        this.url = url + "?TopicCount=10" + "&UserId=" + Functions.getUserId(context);
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONArray jsonArray=new JSONArray(response.getData());

            if (jsonArray != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
                List<Integer> topics = new ArrayList<>();
                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject topicObject = jsonArray.getJSONObject(i);

                        int topicId = topicObject.getInt("pk");
                        topics.add(topicId);
                    }
                }
                MeancoApplication.followedTopicList.clear();
                MeancoApplication.followedTopicList.addAll(topics);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Connect.APIResult doInBackground(Void... voids) {
        Connect.APIResult jsonObject = null;
        try {
            Connect connect = new Connect(url, "GET");
            jsonObject = connect.getJson();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jsonObject;
    }
}
