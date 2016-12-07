package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.TopicDetailActivityFragment;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;

/**
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
                        if(databaseHelper.getComment(commentId)==null) {
                            JSONArray versions = topicObject.getJSONArray("versions");
                            JSONObject contentObject = versions.getJSONObject(i);
                            String content = contentObject.getString("content");
                            Comment c = new Comment(commentId, topicId, content);
                            databaseHelper.addComment(c);
                        }

                    }
                }
                TopicDetailActivityFragment.updateAdapter(databaseHelper, topicId);
            }
            //TODO: Add the new comments into adapter
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
