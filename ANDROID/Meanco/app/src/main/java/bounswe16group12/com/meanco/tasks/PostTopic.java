package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bounswe16group12.com.meanco.database.DatabaseHelper;
import bounswe16group12.com.meanco.objects.Relation;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by Ezgi on 12/5/2016.
 */

public class PostTopic extends AsyncTask<Void, Void, Connect.APIResult> {

    private Topic topic;
    private Context context;
    private final String url = "http://46.101.253.73:8000/API/AddTopic/";

    public PostTopic(Topic topic, Context context){
        this.context = context;
        this.topic = topic;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONObject jsonObject=new JSONObject(response.getData());
            Log.i("JSON GET", response.getData());
            if (jsonObject != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    int topicId = jsonObject.getInt("topic");
                    topic.topicId = topicId;
                    DatabaseHelper db = DatabaseHelper.getInstance(context);
                    db.addTopic(topic);
                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    protected Connect.APIResult doInBackground(Void... void) {

       // String urlParameters  = "name="+topic.topicName+"&tag="+topic.tags.get(0).tagName+"&description="+topic.topicName+"&URL="+topic.topicName;
  //      byte[] postData       = urlParameters.getBytes();
      //  int    postDataLength = postData.length;
        URL url            = null;
        try {
            url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("name",topic.topicName);
            conn.setRequestProperty("tag",topic.tags.get(0).tagName);
            conn.setRequestProperty("description",topic.topicName);
            conn.setRequestProperty("URL",topic.topicName);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            //conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; //TODO: Return response
    }
}
