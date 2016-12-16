package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
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
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by feper on 12/16/2016.
 */

public class FollowTopic extends AsyncTask<Void,Void,Connect.APIResult> {

    private int topicId;
    private Context context;
    private int userId;
    private String url;



    public FollowTopic(String url,int topicId , Context context){
        this.url = url;
        this.topicId = topicId;
        this.userId = Functions.getUserId(context);
        this.context = context;
    }


    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        String responseStr = response.getData();
        if (responseStr != null) {
            if (response.getResponseCode() == 200) {
                Log.i("FOLLOW_TOPIC",responseStr);
                new GetFollowedTopics(MeancoApplication.GET_FOLLOWED_TOPICS_URL,context).execute();
            }
        }

    }

    protected Connect.APIResult doInBackground(Void... voids) {

        String data = null;
        try {
            data = URLEncoder.encode("TopicId", "UTF-8")
                    + "=" + URLEncoder.encode("" + topicId, "UTF-8");
            data += "&" + URLEncoder.encode("UserId", "UTF-8") + "="
                    + URLEncoder.encode("" + userId, "UTF-8");

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
