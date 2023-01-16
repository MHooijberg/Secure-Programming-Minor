package devlink_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

//import org.apache.commons.io.IOUtils;

public class APIManager {
    private String apiVersion;
    private String baseUrl;
    private String defaultMethod;
    private Integer[] okResponseCodes = new Integer[]{304};
    private final String USER_AGENT = "DevLink";

    public APIManager(String apiVersion, String baseUrl, String defaultMethod, Integer[] okResponseCodes) {
        this.apiVersion = apiVersion;
        this.baseUrl = baseUrl;
        this.defaultMethod = defaultMethod;
        this.okResponseCodes = okResponseCodes;
    }

    public HttpsURLConnection RequestBuilder(String endpoint, HashMap<String, String> parameters, String requestMethod) throws IllegalArgumentException, IOException {
        String apiUrl = baseUrl + endpoint;
        if (requestMethod == "GET" || requestMethod == null)
        {
            if (parameters.size() > 0)
            {
                apiUrl += "?";
                for (Map.Entry<String, String> entry : parameters.entrySet())
                    apiUrl += URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
            }
        }

        URL url = new URL(apiUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestProperty("Content-Encoding", "gzip");
        connection.setRequestProperty("Useragent", USER_AGENT);
        
        if (requestMethod == "GET" || requestMethod == null)
            connection.setRequestMethod(defaultMethod);

        else if (requestMethod == "POST")
        {
            connection.setRequestMethod(requestMethod);
            if (parameters.size() > 0)
            {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                StringBuilder streamParameters = new StringBuilder();
                
                for(Map.Entry<String, String> entry : parameters.entrySet())
                {
                    streamParameters.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    streamParameters.append("=");
                    streamParameters.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    streamParameters.append("&");
                }

                writer.write(streamParameters.toString());
                writer.flush();
                writer.close();
                outputStream.close();
            }

            return connection;
        }

        else
            throw new IllegalArgumentException("Unsupported Request Method.");

        return connection;
    }

    public String ResponseHandler(HttpsURLConnection connection) throws IOException, UnsupportedOperationException {
        if(Arrays.asList(okResponseCodes).contains(connection.getResponseCode()))
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
            
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }
        throw new IOException("Response Code not in OK_RESPONSE_CODE list.");
    }
}