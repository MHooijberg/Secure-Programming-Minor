package devlink_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.http.client.utils.URLEncodedUtils;

import com.google.api.Endpoint;
import com.google.cloud.testing.BaseEmulatorHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GitHubManager {
    /* GitHub API Manager:
     * Should manage requests and data conversion from responses for the program to handle.
     * 
     * Functions:
     *      - Search Users, Groups and orginisations
     *      - Search Repositories
     *      - Get commits from repositories
     *  
     * Possible functions:
     *      - Get Private Repositories.
     *      - Notifications of new commits and changes of a repository.
     * 
     * Steps:
     *      - Get data and parameters to get the specific request.
     *      - Create a request and fetch data.
     * 
     */
    public enum DataType{
        Commit,
        Repository,
        User
    }
    
    private APIManager apiManager;
    private HashMap<String, String> endpoints;
    private Gson gson;

    public GitHubManager(){
        apiManager = new APIManager(null, "https://api.github.com/", "GET", new Integer[]{200, 304});

        gson = new GsonBuilder()
        .setLenient()
        .serializeNulls()
        .create();

        this.endpoints = new HashMap<String, String>();
        this.endpoints.put("search commits", "search/commits");
        this.endpoints.put("search repositories", "search/repositories");
        this.endpoints.put("search users", "search/users");
        this.endpoints.put("get commit", "repos/{owner}/{repo}/commits/{ref}");
        this.endpoints.put("get repository", "/repos/{owner}/{repo}");
        this.endpoints.put("get user", "/users/{username}");
    }
    
    public String Search(DataType type, String query)
    {
        String body = "Error: Bad GitHub API Call";
        try {
            HttpsURLConnection connection = null;
            if(type == DataType.Commit){
                connection = apiManager.RequestBuilder(endpoints.get("search commit"), new HashMap<String, String>(){{put("q", query);}}, "GET");
                String apiResult = apiManager.ResponseHandler(connection);

            }
            else if(type == DataType.Repository){
                connection = apiManager.RequestBuilder(endpoints.get("search repository"), new HashMap<String, String>(){{put("q", query);}}, "GET");
                String apiResult = apiManager.ResponseHandler(connection);

            }
            else if(type == DataType.User){
                connection = apiManager.RequestBuilder(endpoints.get("search users"), new HashMap<String, String>(){{put("q", query);}}, "GET");
                String apiResult = apiManager.ResponseHandler(connection);

            }
        }
        catch (IllegalArgumentException | IOException | UnsupportedOperationException e){
            System.out.println(e);
        }
        return body;
    }

    public String Get(String owner, String repository, String reference)
    {
        String body = "Error: Bad GitHub API Call";
        try {
            HttpsURLConnection connection = apiManager.RequestBuilder(endpoints.get("get commit").replace("{owner}", owner).replace("{repo}", repository).replace("{ref}", reference), null, "GET");
            String json = apiManager.ResponseHandler(connection);
            
            Commit apiResult = gson.fromJson(json, Commit.class);

            body = java.text.MessageFormat.format(
                    "==== Commit ====\n" +
                    "Link: {0}\n" +
                    "SHA: {1}\n" +
                    "Author Name: {2}\n" +
                    "Author Email: {3}\n" +
                    "Committer Name: {4}\n" +
                    "Committer Email: {5}\n" +
                    "Message: {6}\n",
                    apiResult.getHtml_url(),
                    apiResult.getSha(),
                    apiResult.getAuthor().getName(),
                    apiResult.getAuthor().getEmail(),
                    apiResult.getCommitter().getName(),                    
                    apiResult.getCommitter().getEmail(),
                    apiResult.getMessage()                    
                );
            
        }
        catch (IllegalArgumentException | IOException | UnsupportedOperationException e){
            System.out.println(e);
        }
        return body;
    }
    public String Get(String owner, String repository)
    {
        String body = "Error: Bad GitHub API Call";
        try {
            HttpsURLConnection connection = apiManager.RequestBuilder(endpoints.get("get repository").replace("{owner}", owner).replace("{repo}", repository), null, "GET");
            String json = apiManager.ResponseHandler(connection);
            
            Repository apiResult = gson.fromJson(json, Repository.class);

            body = java.text.MessageFormat.format(
                    "==== Repository ====\n" +
                    "Id: {0}\n" +
                    "Owner name: {1}\n" +
                    "Is Private: {2}\n" +
                    "Name: {3}\n" +
                    "Title: {4}\n" +
                    "Description: {5}\n" +
                    "Link: {6}\n",
                    apiResult.getId(),
                    apiResult.getOwner().getName(),
                    apiResult.getIsPrivate(),
                    apiResult.getName(),
                    apiResult.getTitle(),
                    apiResult.getDescription(),
                    apiResult.getHtml_url()                    
                );
        }
        catch (IllegalArgumentException | IOException | UnsupportedOperationException e){
            System.out.println(e);
        }
        return body;
    }
    public String Get(String username)
    {
        String body = "Error: Bad GitHub API Call";
        try {
            HttpsURLConnection connection = apiManager.RequestBuilder(endpoints.get("get users").replace("{username}", username), null, "GET");
            String json = apiManager.ResponseHandler(connection);
            
            GitHubUser apiResult = gson.fromJson(json, GitHubUser.class);

            body = java.text.MessageFormat.format(
                    "==== User ====\n" +
                    "Id: {0}\n" +
                    "User Name: {1}\n" +
                    "Company: {2}\n" +
                    "Location: {3}\n" +
                    "Email: {4}\n" +
                    "Bio: {5}\n" +
                    "Blog: {6}\n" +
                    "Link: {7}\n",
                    apiResult.getId(),
                    apiResult.getName(),
                    apiResult.getCompany(),
                    apiResult.getLocation(),
                    apiResult.getEmail(),
                    apiResult.getBio(),
                    apiResult.getBlog(),
                    apiResult.getHtml_url()                    
                );
        }
        catch (IllegalArgumentException | IOException | UnsupportedOperationException e){
            System.out.println(e);
        }
        return body;
    }
}
