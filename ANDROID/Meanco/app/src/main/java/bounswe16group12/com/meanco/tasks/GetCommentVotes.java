package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Vote;
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by feper on 12/16/2016.
 */

public class GetCommentVotes extends AsyncTask<Void,Void,Connect.APIResult>{

    private Context context;
    private String url;
    private int topicId;

    public GetCommentVotes(String url, int topicId, Context context){
        this.context = context;
        this.url = url + "&UserId=" + Functions.getUserId(context) + "&TopicId=" + topicId;
        this.topicId = topicId;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONArray jsonArray=new JSONArray(response.getData());

            if (jsonArray != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject topicObject = jsonArray.getJSONObject(i);
                        JSONObject voteObject = topicObject.getJSONObject("fields");
                        int commentId = voteObject.getInt("comment");
                        boolean isUpvoted = voteObject.getBoolean("upvoted");
                        boolean isDownvoted = voteObject.getBoolean("downvoted");

                        Vote v = new Vote(commentId,isUpvoted,isDownvoted);
                        databaseHelper.addVote(v);
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
