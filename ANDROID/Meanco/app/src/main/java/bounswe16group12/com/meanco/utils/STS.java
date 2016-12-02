package bounswe16group12.com.meanco.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ezgi on 12/2/2016.
 */

public class STS {
    public static String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
            } finally {
                is.close();
            }
            str = sb.toString();
        }
        return str;
    }
}
