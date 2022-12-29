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

    // constructor to that will set dataset and create the dataset catalog based on key names. Example: "03760ED"
    public static void main(String dataset) throws MalformedURLException { 
        //set dataset key in variable for later use
        datasetID = dataset;
        // get dataset call options
        getBaseInformation(datasetID);
        // use function to set json items in datasetArray
        setDatasetCatalog(baseJson); 

    }
    
    // function that will request JSON from CBS
    public static String getJsonFromUrl(String Dataset) throws MalformedURLException
    {
        // Set URL object for data retrieval
        URL url = new URL(Dataset);
        // setting up a string builder to convert data to string
        StringBuilder builder = new StringBuilder();

        // get data from CBS and storing it in builder
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()))) {
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

    //function to get Dataset content overview
    public static String getBaseInformation(String ID) throws MalformedURLException {
        //set link to specifik Dataset
        String combineUrl = baseUrl + ID;
        //Get content from CBS
        baseJson = getJsonFromUrl(combineUrl);

        return baseJson;
    }

    //function to get specific data from the Dataset
    public static String getInformation(String content) throws MalformedURLException {
        // TO DO: Check if request matches Dataset
        if(datasetArray.contains(content)) {
            //Set content link
            String informationID = baseUrl + datasetID + "/" + content;
            //Get content from CBS
            String itemJson = getJsonFromUrl(informationID);
            System.out.println(itemJson);
            
            return itemJson;
        } else {
            // returns error if content request does not match data in datasetArray
            return "Error: Data can not be found";
        }
    }

    // function to set arrayList with every Json call for this dataset
    public static void setDatasetCatalog(String dataset) {
        // convert Json String to object
        JsonObject myjson = new Gson().fromJson(dataset, JsonObject.class);
        // convert Json object to Json array
        JsonArray the_json_Array = myjson.getAsJsonArray("value");

        // For loop to set all Json object to arraylist for use in other functions
        for (JsonElement element : the_json_Array) {
            // makes sure that object is json
            JsonObject jsonElement = element.getAsJsonObject();
            // converts json object to string
            String elementName = jsonElement.get("name").getAsString();
            // set string in ArrayList
            datasetArray.add(elementName);
        }
    }

    // get dataset calls in ArrayList
    public static ArrayList<String> getDatasetCalls() { 
        return datasetArray;
    }

    // get dataset calls in Json format
    public static String getDatasetJson() { 
        return baseJson;
    }

}
