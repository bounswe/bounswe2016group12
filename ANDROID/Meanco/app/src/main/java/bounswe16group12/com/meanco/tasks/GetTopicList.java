package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;


/**
 * Created by Ezgi on 12/2/2016.
 */

public class GetTopicList extends AsyncTask<Void, Void, Connect.APIResult> {

    private Context context;
    private String url;
    public GetTopicList(String url, Context context){
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONArray jsonArray=null;

            if (jsonArray != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject topicObject = jsonArray.getJSONObject(i);
                        int topicId = topicObject.getInt("id");
                        JSONObject nameObject = topicObject.getJSONObject("name");
                        String topicName = topicObject.getString("label");

                        Topic t = new Topic(topicName, null);
                        databaseHelper.addOrUpdateTopic(t);


                    }
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
