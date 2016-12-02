package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by Ezgi on 12/2/2016.
 */

public class GetTopicDetail extends AsyncTask<Void, Void, Connect.APIResult> {

    private Context context;
    private String url;
    public GetTopicDetail(String url, Context context){
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONObject jsonObject=null;

            if (jsonObject != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    Comment comment = new Comment();
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
