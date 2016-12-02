package bounswe16group12.com.meanco.utils;

/**
 * Created by Ezgi on 12/2/2016.
 */

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connect {

    private String url;
    private String requestMethod;
    private boolean doInput;
    private boolean doOutput;
    private OutputStreamWriter writer;
    private HttpURLConnection urlConnection;

    public Connect(String url, String requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public boolean isDoInput() {
        return doInput;
    }

    public void setDoInput(boolean doInput) {
        this.doInput = doInput;
    }

    public boolean isDoOutput() {
        return doOutput;
    }

    public void setDoOutput(boolean doOutput) {
        this.doOutput = doOutput;
    }

    private void write(String url) throws IOException {
        if (urlConnection != null) {
            writer = new OutputStreamWriter(urlConnection.getOutputStream());
            writer.write(url);
            writer.flush();
        }
    }

    public APIResult getJson() throws Exception {
        URL url1 = new URL(url);
        urlConnection = (HttpURLConnection) url1.openConnection();
        urlConnection.setRequestMethod(requestMethod);
        return new APIResult(urlConnection.getResponseCode(), STS.streamToString(urlConnection
                .getInputStream()));

    }



    public static class APIResult{
        int responseCode;
        String data;

        public APIResult(int responseCode, String data) {
            this.responseCode = responseCode;
            this.data = data;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }


}

