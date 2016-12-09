package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.adapters.CustomHomeAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
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
            JSONArray jsonArray=new JSONArray(response.getData());
            Log.i("JSON GET", response.getData());
            if (jsonArray != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++){

                        ArrayList<Tag> tagsArray = new ArrayList<>();
                        JSONObject topicObject = jsonArray.getJSONObject(i);

                        //related to topic name
                        int topicId = topicObject.getInt("id");
                        String topicName = topicObject.getString("label");

                        //related to relations
                        JSONArray relationsTo = topicObject.getJSONArray("relations_a");


                        for(int j=0; j<relationsTo.length(); j++){
                            JSONObject relationObject = relationsTo.getJSONObject(j);
                            int relationId = relationObject.getInt("id");
                            //vote count
                            //int voteCount = relationObject.getInt(vote_count);
                            String label = relationObject.getString("label");
                            int topicToId = relationObject.getInt("topic_b");
                            boolean isBidirectional = relationObject.getBoolean("isBidirectional");
                            Relation r = new Relation(relationId, label, topicId, topicToId, isBidirectional);
                            databaseHelper.addRelation(r);
                        }

                        //related to tag
                        JSONArray tags = topicObject.getJSONArray("tags");

                        for(int j=0; j<tags.length(); j++){
                            JSONObject tagObject = tags.getJSONObject(j).getJSONObject("tag");

                            int tagId = tagObject.getInt("id");
                            String label = tagObject.getString("label");
                            String description = tagObject.getString("description");
                            String URL = tagObject.getString("URL");
                            Tag t = new Tag(tagId, description, label,URL);
                            tagsArray.add(t);

                            if(databaseHelper.getTag(tagId)==null) {
                                databaseHelper.addTag(t);
                            }
                        }
                        Topic t = new Topic(topicId, topicName, tagsArray);
                        databaseHelper.addTopic(t);
                    }
                }
                HomeActivityFragment.adapter.clear();
                HomeActivityFragment.adapter.updateArray();
                HomeActivityFragment.adapter.notifyDataSetChanged();
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
