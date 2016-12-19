package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
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
    private boolean isUserCommentTask;

    public GetTopicDetail(String url, int topicId,boolean isUserCommentTask, Context context){
        this.context = context;
        this.url = url + topicId;
        this.topicId = topicId;
        this.isUserCommentTask = isUserCommentTask;
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
                        int voteCount = topicObject.getInt("vote_count");
                        JSONArray versions = topicObject.getJSONArray("versions");
                        JSONObject contentObject = versions.getJSONObject(0);
                        String content = contentObject.getString("content");
                        String dateStr = contentObject.getString("timestamp");
                        try{
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = dateFormat.parse(dateStr);
                            long time = date.getTime();

                            Comment c = new Comment(commentId, topicId, content,username,time,voteCount);
                            databaseHelper.addComment(c);
                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                    }

                    if(Functions.getUserId(context)!=-1) {
                        if(!isUserCommentTask)
                            new GetCommentVotes(MeancoApplication.GET_COMMENT_VOTES_URL, topicId, context).execute();
                    }
                }
                if(TopicDetailActivityFragment.mCommentsAdapter != null)
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
