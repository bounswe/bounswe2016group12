package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by Ezgi on 12/5/2016.
 */

public class PostTag extends AsyncTask<String, Void, Void> {
    private Tag tagToPost = null;// post data

    private Context context;
    private String url;



    public PostTag(String url, Context context, Tag tagToPost){
        this.context = context;
        this.url = url;
        this.tagToPost = tagToPost;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(String... strings) {

        String urlParameters  = "id="+tagToPost.tagId+"&label="+tagToPost.tagName+"&description="+tagToPost.context;
        byte[] postData       = urlParameters.getBytes();
        int    postDataLength = postData.length;
        URL url            = null;
        try {
            url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
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
        return null;
    }
}
