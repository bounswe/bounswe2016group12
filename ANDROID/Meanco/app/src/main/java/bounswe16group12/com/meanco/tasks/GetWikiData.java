package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bounswe16group12.com.meanco.activities.TagSearchActivity;
import bounswe16group12.com.meanco.adapters.TagSearchAdapter;
import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.fragments.home.HomeActivityFragment;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by Ezgi on 12/7/2016.
 */

public class GetWikiData extends AsyncTask<Void, Void, Connect.APIResult > {

    private String url;
    private Context context;

    public GetWikiData(String url, Context context) {
        this.url = url;
        this.context = context;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {

            TagSearchAdapter.wikiTags.clear();
            TagSearchAdapter.wikiTags.addAll(TagSearchActivity.checkedTags);
            Log.d("wiki response", response.getData());
           // Log.d("wiki tags after clear", );
            JSONArray jsonArray=new JSONObject(response.getData()).getJSONArray("search");

            if (jsonArray != null) {

                if (response.getResponseCode() == 200) {
                    for(int i=0;i<jsonArray.length();i++) {


                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        if(!obj.has("description") || obj.getString("description").equals("Wikipedia disambiguation page")
                                || obj.getString("description").equals("Wikimedia disambiguation page"))
                            continue;
                        String url = obj.getString("concepturi");
                        String label = obj.getString("label");
                        String description = obj.getString("description");


                        Tag t = new Tag(-1, description, label, url);
                        TagSearchAdapter.wikiTags.add(t);

                    }
                }

                TagSearchActivity.adapter.clear();
                TagSearchActivity.adapter.updateArray();
                TagSearchActivity.adapter.notifyDataSetChanged();


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
