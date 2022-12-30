package DevLink_Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CBSManager {

    // Start URL link for any open data request
    private static String baseUrl = "https://opendata.cbs.nl/ODataApi/OData/";
    // will store the specifik data item that a user requests
    private static String datasetID;
    // String that will store the base Json object.
    private static String baseJson;
    // Arraylist that will hold all possible calls for the dataset
    private static ArrayList<String> datasetArray = new ArrayList<String>();
    
    // Function that will request JSON from CBS
    public static String getJsonFromUrl(String Dataset) throws MalformedURLException
    {
        // Set URL object for data retrieval
        URL url = new URL(Dataset);
        // Setting up a string builder to convert data to string
        StringBuilder builder = new StringBuilder();

        // Get data from CBS and storing it in builder
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String str;
            // Check if return if not null
            while ((str = bufferedReader.readLine()) != null) {
                // Add string to builder
                builder.append(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Set string
        String jsonStr = builder.toString();

        // Return json as a string
        return jsonStr;
    }

    // Function to get Dataset content overview
    public static String getJsonLayoutInformation(String dataset) throws MalformedURLException {
        // Set link to specifik Dataset
        String combineUrl = baseUrl + dataset;
        // Get content from CBS
        baseJson = getJsonFromUrl(combineUrl);

        // Returns the layout of the dataset in json string
        return baseJson;
    }

    // Function to set arrayList with every Json call for this dataset
    public static void setDatasetCatalog(String dataset) {
        // Convert Json String to object
        JsonObject myJson = new Gson().fromJson(dataset, JsonObject.class);
        // Convert Json object to Json array
        JsonArray jsonArray = myJson.getAsJsonArray("value");

        // For loop to set all Json object to arraylist for use in other functions
        for (JsonElement element : jsonArray) {
            // Makes sure that object is json
            JsonObject jsonElement = element.getAsJsonObject();
            // Converts json object to string
            String elementName = jsonElement.get("name").getAsString();
            // Set string in ArrayList
            datasetArray.add(elementName);
        }
    }

    // Get dataset calls in ArrayList
    public static ArrayList<String> getDatasetCalls(String dataset) throws MalformedURLException { 
        // Retrieves Json string with dataset key
        baseJson = getJsonLayoutInformation(dataset);
        // Convert Json string in call option arraylist
        setDatasetCatalog(baseJson);
        
        // Return arrayList with call options
        return datasetArray;
    }

    // Function to get specific data from the Dataset
    public static String getInformation(String dataset, String content) throws MalformedURLException {
        // Retrieves Json string with dataset key
        baseJson = getJsonLayoutInformation(dataset);
        // Convert Json string in call option arraylist
        setDatasetCatalog(baseJson);

        // Check if request matches Dataset
        if(datasetArray.contains(content)) {
            // Set content link
            String informationID = baseUrl + dataset + "/" + content;
            // Get content from CBS
            String itemJson = getJsonFromUrl(informationID);
            
            // Return Json information
            return itemJson;
        } else {
            // Returns error if content request does not match data in datasetArray
            return "Error: Data can not be found";
        }
    }
}
