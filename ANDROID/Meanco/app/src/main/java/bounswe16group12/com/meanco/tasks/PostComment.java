package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

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
import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.utils.Connect;
import bounswe16group12.com.meanco.utils.Functions;

/**
 * Created by Ezgi on 12/5/2016.
 */

public class PostComment extends AsyncTask<Void,Void,Connect.APIResult>{
    private Comment comment;
    private Context context;
    private int userId;
    private String url;



    public PostComment(String url,Comment comment , Context context){
        this.url = url;
        this.comment = comment;
        this.userId = Functions.getUserId(context);
        this.context = context;
    }


    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONObject jsonObject=new JSONObject(response.getData());
            if (jsonObject != null) {
                if (response.getResponseCode() == 200) {
                    new GetTopicDetail(MeancoApplication.SITE_URL,comment.topicId,context).execute();
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    protected Connect.APIResult doInBackground(Void... voids) {

        String data = null;
        try {
            data = URLEncoder.encode("topicId", "UTF-8")
                    + "=" + URLEncoder.encode("" + comment.topicId, "UTF-8");
            data += "&" + URLEncoder.encode("userId", "UTF-8") + "="
                    + URLEncoder.encode("" + userId, "UTF-8");

            data += "&" + URLEncoder.encode("text", "UTF-8")
                    + "=" + URLEncoder.encode(comment.content,"UTF-8");

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
