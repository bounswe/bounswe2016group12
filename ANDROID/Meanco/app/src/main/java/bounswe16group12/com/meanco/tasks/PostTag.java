package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import bounswe16group12.com.meanco.MeancoApplication;
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.activities.TagSearchActivity;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by Ezgi on 12/5/2016.
 */

public class PostTag extends AsyncTask<Void, Void, Connect.APIResult> {
    private Tag tagToPost = null;// post data
    private int topicId;
    private String url;
    private boolean isLast;
    private Context context;

    public PostTag(String url, Tag tagToPost, int topicId, boolean isLast,Context context){
        this.url = url;
        this.tagToPost = tagToPost;
        this.topicId = topicId;
        this.isLast = isLast;
        this.context = context;
    }


    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        if (response != null) {
            Log.i("TAG_POST_REQUEST","CODE: " + response.getResponseCode() + "   DATA: " + response.getData());
        }
        if(isLast){
            Log.i("TAG_POST","FINISHED");
            TagSearchActivity.checkedTags.clear();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Connect.APIResult doInBackground(Void... voids) {

        String data = null;
        try {
            data = URLEncoder.encode("topicId", "UTF-8")
                    + "=" + URLEncoder.encode(topicId +"", "UTF-8");
            data += "&" + URLEncoder.encode("label", "UTF-8") + "="
                    + URLEncoder.encode(tagToPost.tagName, "UTF-8");

            data += "&" + URLEncoder.encode("description", "UTF-8")
                    + "=" + URLEncoder.encode(tagToPost.context, "UTF-8");

            data += "&" + URLEncoder.encode("URL", "UTF-8")
                    + "=" + URLEncoder.encode(tagToPost.URL, "UTF-8");

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
