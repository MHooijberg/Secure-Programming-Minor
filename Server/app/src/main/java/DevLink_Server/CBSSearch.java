package DevLink_Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



public class CBSSearch {

    // Arraylist to contain all datasetCollection keys
    private static ArrayList<String> datasetCollenction = new ArrayList<String>();
    // HashMap to store search results
    private static HashMap<String, String> searchResults = new HashMap<String, String>();
    
    // Function to retrieve all dataset collection keys
    public static void setDatasetCollection() throws IOException {
        URL url = new URL("https://opendata.cbs.nl/odataapi");
        // Retrieving the contents of the specified page
        Scanner sc = new Scanner(url.openStream());
        // Instantiating the StringBuffer class to hold the result
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next());
        }
        // Retrieving the String from the String Buffer object
        String result = sb.toString();
        // Removing the HTML tags
        result = result.replaceAll("<[^>]*>", "");
        // Remove unnessesarry data and insert remaining in a array
        String[] resultArray = result.split("/ODataApi/OData");

        // Set array collection in arraylist for later use
        for(int i = 1; i < resultArray.length; i++ ) {
            datasetCollenction.add(resultArray[i]);
        }
    }

    // function that will request JSON from CBS
    public static String getJsonFromUrl(String Dataset) throws IOException
    {
        // Start URL link for any open data request
        String urlString = "https://opendata.cbs.nl/ODataApi/OData/" + Dataset;
        // Set URL object for data retrieval
        URL url = new URL(urlString);
        
        // Setup for HTTP connection
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("GET");

        // setting up a string builder to convert data to string
        StringBuilder builder = new StringBuilder();

        // get data from CBS and storing it in builder
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
            String str;
            // check if return if not null
            while ((str = bufferedReader.readLine()) != null) {
                // add string to builder
                builder.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Set string
        String jsonStr = builder.toString();

        return jsonStr;
    }

    // Function to search for datasets
    public static HashMap<String, String> searchQuery(String search) throws IOException {
        // Set all dataset keys to be searched in
        setDatasetCollection();

        // For loop that will search through all dataset keys
        for(String item : datasetCollenction) {
            // Add try, so search can complete after false returns
            try{
                // Get Json string containing all table information
                String jsonResult = getJsonFromUrl(item + "/TableInfos");
        
                // Convert Json String to object
                JsonObject myJson = new Gson().fromJson(jsonResult, JsonObject.class);
                // Convert Json object to Json array
                JsonArray jsonArray = myJson.getAsJsonArray("value");

                // For loop to set all Json object to arraylist for use in other functions
                for (JsonElement element : jsonArray) {
                    // Makes sure that object is json
                    JsonObject jsonElement = element.getAsJsonObject();
                    // Converts json object to string
                    String elementName = jsonElement.get("Identifier").getAsString() + " - " + jsonElement.get("Title").getAsString() + " - " + jsonElement.get("ShortDescription").getAsString();
                    
                    // Search if word can be found in the table information
                    if(elementName.contains(search)){
                        // Set result key and title in Hasmap 
                        searchResults.put(jsonElement.get("Identifier").getAsString(), jsonElement.get("Title").getAsString());
                        // Limit return to three results
                        if(searchResults.size() == 3) {
                            return searchResults;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }

        // return in case that less than three results can be given
        return searchResults;

    }

}
