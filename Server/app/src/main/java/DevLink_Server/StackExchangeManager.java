package devlink_server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import javax.net.ssl.HttpsURLConnection;

import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.storage.v2.QueryWriteStatusRequest;

public class StackExchangeManager {
    /* StackExchange API Manager:
     * Should manage requests and data conversion from responses for the program to handle.
     * 
     * Functions:
     *      - Search a question
     *      - Get a question, anwer and user
     * 
     * Steps:
     *      - Get data and parameters to get the specific request.
     *      - Create a request and fetch data.
     * 
     * Search results: Title, Link, ID, body start
     * Get Question: Title, Link, ID, full body, upvotes, answers.
     * Get Accepted Answer:  body, score, user, title
     * Get User: Name, about, reputation
     */
    public enum DataType{
        Answer,
        Question,
        User
    }

    private APIManager apiManager;
    private HashMap<String, String> endpoints;
    private Gson gson;
    
    public StackExchangeManager(){
        apiManager = new APIManager(null, "https://api.stackexchange.com/", "GET", new Integer[]{200, 304});
        
        gson = new GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create();

        this.endpoints = new HashMap<String, String>();
        this.endpoints.put("search", "search");
        this.endpoints.put("get answer", "answers/{ids}");
        this.endpoints.put("get comment", "comments/{ids}");
        this.endpoints.put("get post", "posts/{ids}");
        this.endpoints.put("get question", "questions/{ids}");
        this.endpoints.put("get user", "users/{ids}");
    }
    
    public String Search(String query)
    {
        String body = "Error: Bad StackExchange API Call";
        try {
            HttpsURLConnection connection = apiManager.RequestBuilder(endpoints.get("search"), new HashMap<String, String>(){{put("site", "stackoverflow"); put("intitle", query); put("filter", "withbody");}}, "GET");
            String json = apiManager.ResponseHandler(connection);
            json = json.replace("{\"items\":", "").split(",\"has_more\":false,\"quota_max\":300,\"quota_remaining\"")[0];

            Question[] result = gson.fromJson(json, Question[].class);

            body = "";
        }
        catch (IllegalArgumentException | IOException | UnsupportedOperationException e){
            System.out.println(e);
        }
        return body;
    }

    public String Get(DataType type, String id){
        String body = "Error: Bad API Call";
        try {
            HttpsURLConnection connection = null;
            String result;
            if(type == DataType.Answer){
                connection = apiManager.RequestBuilder(endpoints.get("get answer").replace("{ids}", id), new HashMap<String, String>(){{put("site", "stackoverflow"); put("filter", "withbody");}}, "GET");
                String json = apiManager.ResponseHandler(connection);

                json = json.replace("{\"items\":[", "").split("],\"has_more\":false,\"quota_max\":300,\"quota_remaining\"")[0]; 
                Answer apiResult = gson.fromJson(json, Answer.class);

                body = java.text.MessageFormat.format(
                    "==== Answer ====\n" +
                    "Title: {0}\n" +
                    "Body: {1}\n\n" +
                    "Author: {2}\n" +
                    "Author Id: {3}\n" +
                    "Down votes: {4}\n",
                    "Up votes: {5}\n",
                    "Link: {6}\n",
                    apiResult.getTitle(),
                    apiResult.getBody(),
                    apiResult.getOwner().getDisplay_name(),
                    apiResult.getOwner().getAccount_id(),
                    apiResult.getDown_vote_count(),
                    apiResult.getUp_vote_count(),
                    apiResult.getLink()                    
                );
            }
            else if(type == DataType.Question){
                connection = apiManager.RequestBuilder(endpoints.get("get question").replace("{ids}", id), new HashMap<String, String>(){{put("site", "stackoverflow"); put("filter", "withbody");}}, "GET");
                String json = apiManager.ResponseHandler(connection);

                json = json.replace("{\"items\":[", "").split("],\"has_more\":false,\"quota_max\":300,\"quota_remaining\"")[0];
                Question apiResult = gson.fromJson(json, Question.class);

                body = java.text.MessageFormat.format(
                    "==== Question ====\n" +
                    "Title: {0}\n" +
                    "Body: {1}\n\n" +
                    "Author: {2}\n" +
                    "Author Id: {3}\n" +
                    "View Count: {4}\n",
                    "Up votes: {5}\n",
                    "Link: {6}\n",
                    apiResult.getTitle(),
                    apiResult.getBody(),
                    apiResult.getOwner().getDisplay_name(),
                    apiResult.getOwner().getAccount_id(),
                    apiResult.getView_count(),
                    apiResult.getUp_vote_count(),
                    apiResult.getLink()                    
                );
            }
            else if(type == DataType.User){
                connection = apiManager.RequestBuilder(endpoints.get("get users").replace("{ids}", id), new HashMap<String, String>(){{put("site", "stackoverflow");}}, "GET");
                String json = apiManager.ResponseHandler(connection);
                
                json = json.replace("{\"items\":[", "").split("],\"has_more\":false,\"quota_max\":300,\"quota_remaining\"")[0];
                ShallowUser apiResult = gson.fromJson(json, ShallowUser.class);

                body = java.text.MessageFormat.format(
                    "==== User ====\n" +
                    "Name: {0}\n" +
                    "User Type: {1}\n" +
                    "Reputation: {2}\n" +
                    "Accept Rate: {3}\n" +
                    "Link: {6}\n",
                    apiResult.getDisplay_name(),
                    apiResult.getUser_type(),
                    apiResult.getReputation(),
                    apiResult.getAccept_rate(),
                    apiResult.getLink()                    
                );
            }
            //System.out.println(result);
            
        }
        catch (IllegalArgumentException | IOException | UnsupportedOperationException e){
            System.out.println(e);
        }
        return body;
    }
}
