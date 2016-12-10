package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bounswe16group12.com.meanco.activities.TagSearchActivity;
import bounswe16group12.com.meanco.activities.TopicSearchActivity;
import bounswe16group12.com.meanco.adapters.TagSearchAdapter;
import bounswe16group12.com.meanco.adapters.TopicSearchAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by Ezgi on 12/10/2016.
 */

public class SearchTask  extends AsyncTask<Void, Void, Connect.APIResult >
{

    private String url;
    private Context context;

    public SearchTask(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {

            TopicSearchAdapter.relationTopics.clear();
            JSONArray jsonArray=new JSONArray(response.getData());

            if (jsonArray != null) {

                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++) {


                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        int topicId = obj.getInt("pk");

                        if(TopicSearchActivity.fromOrTo.equals("from") || topicId!=TopicSearchAdapter.idFrom) {
                            Topic t = DatabaseHelper.getInstance(context).getTopic(topicId);
                            TopicSearchAdapter.relationTopics.add(t);
                        }

                    }
                }

                TopicSearchActivity.adapter.clear();
                TopicSearchActivity.adapter.updateArray();
                TopicSearchActivity.adapter.notifyDataSetChanged();


            }
        } catch (JSONException e) {

            e.printStackTrace();
        }


    }

    @Override
    protected Connect.APIResult  doInBackground(Void... voids) {
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
