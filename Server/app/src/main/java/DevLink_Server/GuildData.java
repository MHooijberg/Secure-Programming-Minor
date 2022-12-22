package devlink_server;

import java.util.ArrayList;
import java.util.List;
import com.google.cloud.firestore.DocumentReference;

public class GuildData {
    private String gitHubAPIKey;
    private String name;
    private String stackExchangeAPIKey;
    private List<DocumentReference> users;

    // [START firestore_data_custom_type_definition]
    public GuildData(){}

    public GuildData(String gitHubAPIKey, String name, String stackExchangeAPIKey, List<DocumentReference> users){
        this.gitHubAPIKey = gitHubAPIKey;
        this.name = name;
        this.stackExchangeAPIKey = stackExchangeAPIKey;
        this.users = users;
    }

    public GuildData(String gitHubAPIKey, String name, String stackExchangeAPIKey, DocumentReference user){
        this.gitHubAPIKey = gitHubAPIKey;
        this.name = name;
        this.stackExchangeAPIKey = stackExchangeAPIKey;
        this.users = new ArrayList<DocumentReference>();
        this.users.add(user);
    }
    // [END firestore_data_custom_type_definition]

    public String getGitHubAPIKey(){
        return this.gitHubAPIKey;
    }
    public void setGitHubAPIKey(String gitHubAPIKey){
        this.gitHubAPIKey = gitHubAPIKey;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getStackExchangeAPIKey(){
        return stackExchangeAPIKey;
    }
    public void setStackExchangeAPIKey(String stackExchangeAPIKey){
        this.stackExchangeAPIKey = stackExchangeAPIKey;
    }
    public List<DocumentReference> getUsers(){
        return users;
    }
    public void setUsers(List<DocumentReference> users)
    {
        this.users = users;
    }
    public void setUsers(DocumentReference user)
    {
        this.users = new ArrayList<DocumentReference>();
        this.users.add(user);
    }
}
