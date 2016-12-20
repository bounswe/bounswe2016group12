package bounswe16group12.com.meanco.utils;

/**
 * Connect class opens an HttpURLConnection and gets JSON string response and response code from the connection.
 * This method is just used for get requests.
 * Created by Ezgi on 12/2/2016.
 */

import java.net.HttpURLConnection;
import java.net.URL;

public class Connect {

    private String url;
    private String requestMethod;
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

    public APIResult getJson() throws Exception {
        URL url1 = new URL(url);
        urlConnection = (HttpURLConnection) url1.openConnection();
        urlConnection.setRequestMethod(requestMethod);
        return new APIResult(urlConnection.getResponseCode(), Functions.streamToString(urlConnection.getInputStream()));

    }

    public static class APIResult {
        int responseCode;
        String data;

        public APIResult(int responseCode, String data) {
            this.responseCode = responseCode;
            this.data = data;
        }

        public int getResponseCode() {
            return responseCode;
        }


        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}

