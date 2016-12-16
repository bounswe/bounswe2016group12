package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Task for getting comments of a topic.
 * Created by Ezgi on 12/2/2016.
 */

public class GetTopicDetail extends AsyncTask<Void, Void, Connect.APIResult> {

    private Context context;
    private String url;
    private int topicId;
    public GetTopicDetail(String url, int topicId, Context context){
        this.context = context;
        this.url = url + topicId;
        this.topicId = topicId;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONObject jsonObject=new JSONObject(response.getData());

            if (jsonObject != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    JSONArray commentsObject = jsonObject.getJSONArray("comments");
                    for(int i=0;i<commentsObject.length();i++){
                        JSONObject topicObject = commentsObject.getJSONObject(i);
                        int commentId = topicObject.getInt("id");
                        String username = topicObject.getString("profile");
                        JSONArray versions = topicObject.getJSONArray("versions");
                        JSONObject contentObject = versions.getJSONObject(0);
                        String content = contentObject.getString("content");

                        Comment c = new Comment(commentId, topicId, content,username);
                        databaseHelper.addComment(c);
                    }

                    if(Functions.getUserId(context)!=-1) {
                        new GetCommentVotes(MeancoApplication.GET_COMMENT_VOTES_URL, topicId, context).execute();
                    }
                }
                TopicDetailActivityFragment.updateAdapters(databaseHelper, topicId);
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
