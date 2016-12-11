package bounswe16group12.com.meanco.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

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
import bounswe16group12.com.meanco.activities.HomeActivity;
import bounswe16group12.com.meanco.activities.TopicDetailActivity;
import bounswe16group12.com.meanco.utils.Connect;

/**
 * Created by feper on 12/11/2016.
 */

public class Authentication extends AsyncTask<Void, Void, Connect.APIResult> {
    private String email;
    private String username;
    private String password;
    private Context context;
    private String url;

    public Authentication(String url,String email, String username, String password, Context context){
        this.url = url;
        this.email = email;
        this.username = username;
        this.password = password;
        this.context = context;

    }

    @Override
    protected void onPostExecute(Connect.APIResult response) {
        super.onPostExecute(response);

        try {
            JSONObject jsonObject=new JSONObject(response.getData());
            if (jsonObject != null) {
                if (response.getResponseCode() == 200) {
                    int userId = jsonObject.getInt("UserId");

                    SharedPreferences preferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.putInt("UserId", userId);
                    editor.commit();

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Connect.APIResult doInBackground(Void... voids) {

        String data = null;
        try {
            if(url.equals(MeancoApplication.REGISTER_URL)) {
                data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(email + "", "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                        + URLEncoder.encode(username, "UTF-8");

                data += "&" + URLEncoder.encode("password1", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");

                data += "&" + URLEncoder.encode("password2", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");
            }
            else if(url.equals(MeancoApplication.LOGIN_URL)){
                data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(username + "", "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8")
                        + "=" + URLEncoder.encode(password, "UTF-8");
            }

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
