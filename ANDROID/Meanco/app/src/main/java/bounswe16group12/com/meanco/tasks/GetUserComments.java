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
import bounswe16group12.com.meanco.objects.Vote;
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by feper on 12/16/2016.
 */

public class GetUserComments extends AsyncTask<Void,Void,Connect.APIResult> {

    private Context context;
    private String url;
    private int topicId;

    public GetUserComments(String url, int topicId, Context context){
        this.context = context;
        this.url = url + "?TopicCount=10" + "&UserId=" + Functions.getUserId(context);
        this.topicId = topicId;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONArray jsonArray=new JSONArray(response.getData());

            if (jsonArray != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
                List<Comment> comments = new ArrayList<>();
                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject topicObject = jsonArray.getJSONObject(i);

                        int topicId = topicObject.getInt("pk");
                        List<Comment> topicComments = databaseHelper.getAllComments(topicId);
                        for(Comment c : topicComments){
                            if(c.username.equals(Functions.getUsername(context))){
                                comments.add(c);
                            }
                        }
                    }
                    MeancoApplication.topicCommentIds = comments;
                }
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
