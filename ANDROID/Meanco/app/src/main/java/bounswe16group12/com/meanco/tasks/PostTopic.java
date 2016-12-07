package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
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
            Log.i("JSON POST", response.getData());
            if (jsonObject != null) {
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

                if (response.getResponseCode() == 200) {
                    int topicId = jsonObject.getInt("Topic");
                    topic.topicId = topicId;

                    Tag t = new Tag(topic.tags.get(0).tagId,topic.tags.get(1).tagName,topic.tags.get(0).tagName,topic.tags.get(2).tagName);
                    topic.tags.clear();
                    topic.tags.add(t);

                    databaseHelper.addTopic(topic);
                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    protected Connect.APIResult doInBackground(Void... voids) {

        String data = null;
        try {
            //TODO: get(0): topicName get(1): context get(2): URL
            data = URLEncoder.encode("topicName", "UTF-8")
                    + "=" + URLEncoder.encode(topic.topicName, "UTF-8");
            data += "&" + URLEncoder.encode("tag", "UTF-8") + "="
                + URLEncoder.encode(topic.tags.get(0).tagName, "UTF-8");

            data += "&" + URLEncoder.encode("description", "UTF-8")
                + "=" + URLEncoder.encode(topic.tags.get(1).tagName, "UTF-8");

            data += "&" + URLEncoder.encode("URL", "UTF-8")
                + "=" + URLEncoder.encode(topic.topicName + topic.tags.get(2).tagName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String text = "";
        BufferedReader reader=null;
        URL url            = null;
        try {
            url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();

            int responseCode = conn.getResponseCode();
            Log.i("POST_RESPONSE", "" + responseCode);
            Log.i("POST_RESPONSE", text);
            return new Connect.APIResult(responseCode,text);
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
