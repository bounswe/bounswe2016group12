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

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.activities.TagSearchActivity;
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
    private String url;

    public PostTopic(Topic topic, Context context, String url){
        this.context = context;
        this.topic = topic;
        this.url = url;
    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONObject jsonObject=new JSONObject(response.getData());
            if (jsonObject != null) {
                if (response.getResponseCode() == 200) {
                    int topicId = jsonObject.getInt("topicId");
                    topic.topicId = topicId;

                    Log.i("POST_TOPIC : ", topic.tags.size() + " T:  " + topic.tags.toString());
                    topic.tags.remove(0); //Used in add topic task
                    if(topic.tags.size() > 0) {
                        for (Tag tag : topic.tags) {
                            boolean isLast = (topic.tags.size() - topic.tags.indexOf(tag)) == 1;
                            new PostTag(MeancoApplication.POST_TAG_URL, tag, topicId, isLast, context).execute();

                        }
                    }

                   TagSearchActivity.checkedTags.clear();

                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    protected Connect.APIResult doInBackground(Void... voids) {

        String data = null;
        try {
            data = URLEncoder.encode("topicName", "UTF-8")
                    + "=" + URLEncoder.encode(topic.topicName, "UTF-8");
            data += "&" + URLEncoder.encode("tag", "UTF-8") + "="
            + URLEncoder.encode(topic.tags.get(0).tagName, "UTF-8");

            data += "&" + URLEncoder.encode("description", "UTF-8")
                + "=" + URLEncoder.encode(topic.tags.get(0).context, "UTF-8");

            data += "&" + URLEncoder.encode("URL", "UTF-8")
                + "=" + URLEncoder.encode(topic.tags.get(0).URL, "UTF-8");

            Log.i("TOPIC_DATA", data);
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
            conn.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();

            int responseCode = conn.getResponseCode();

            if(responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            else
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;


            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();


            Log.i("RESPONSE_TOPIC",responseCode + " BODY: " + text);
            return new Connect.APIResult(responseCode,text);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
