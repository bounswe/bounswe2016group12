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
 *  * Task for posting tag of a topic to db.
* Tags are posted one at a time.
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
            //new GetTopicDetail(MeancoApplication.SITE_URL,topicId,context).execute();
            TagSearchActivity.checkedTags.clear();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Connect.APIResult doInBackground(Void... voids) {

        //TODO: 50 will be 100 after backend update
        if(tagToPost.context.length() > 50){
            int indexOfPoint = tagToPost.context.indexOf(".");
            int indexOfComma = tagToPost.context.indexOf(",");
            if(indexOfPoint != -1){
                tagToPost.context.substring(0,indexOfPoint);
            }
            else if(indexOfComma != -1){
                tagToPost.context.substring(0,indexOfComma);
            }
            else{
                tagToPost.context.substring(0,48);
            }
        }

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
            conn.setRequestMethod("POST");

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();


            int responseCode = conn.getResponseCode();

            if(responseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            else
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();

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
