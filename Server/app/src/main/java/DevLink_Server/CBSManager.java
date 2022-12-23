package DevLink_Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CBSManager {

    private String baseUrl = "https://opendata.cbs.nl/ODataApi/OData/";

    public void CBSManager(){
        // TODO: Create a constructor.
    }

    public static void getJsonFromUrl(String Dataset) throws MalformedURLException
    {
        String combineUrl = baseUrl + Dataset;
        URL url = new URL(combineUrl);

        StringBuilder builder = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                builder.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String jsonStr = builder.toString();
        System.out.println(jsonStr);
    }

}
