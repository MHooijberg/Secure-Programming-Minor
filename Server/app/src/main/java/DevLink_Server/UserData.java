package devlink_server;

import java.util.ArrayList;
import java.util.List;
import com.google.cloud.firestore.DocumentReference;

public class UserData {
    private String email;
    private List<DocumentReference> guilds;
    private boolean isOnline;
    private String phoneNumber;
    private List<String> registrationToken;
    private String username;

    // [START firestore_data_custom_type_definition]
    public UserData(){}

    public UserData(String email, List<DocumentReference> guilds, boolean isOnline, String phoneNumber, List<String> registrationToken, String username)
    {
        this.email = email;
        this.guilds = guilds;
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.registrationToken = registrationToken;
        this.username = username;
    }
    public UserData(String email, DocumentReference guild, boolean isOnline, String phoneNumber, String registrationToken, String username)
    {
        this.email = email;
        this.guilds = new ArrayList<DocumentReference>();
        this.guilds.add(guild);
        this.isOnline = isOnline;
        this.phoneNumber = phoneNumber;
        this.registrationToken = new ArrayList<String>();
        this.registrationToken.add(registrationToken);
        this.username = username;
    }
    // [END firestore_data_custom_type_definition]

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public List<DocumentReference> getGuilds(){
        return guilds;
    }
    public void setGuilds(List<DocumentReference> guilds){
        this.guilds = guilds;
    }
    public void setGuilds(DocumentReference guild){
        this.guilds = new ArrayList<DocumentReference>();
        this.guilds.add(guild);
    }
    public boolean getIsOnline(){
        return isOnline;
    }
    public void setIsOnline(boolean isOnline){
        this.isOnline = isOnline;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public List<String> getRegistrationToken(){
        return registrationToken;
    }
    public void setRegistrationToken(List<String> registrationToken){
        this.registrationToken = registrationToken;
    }
    public void setRegistrationToken(String registrationToken){
        this.registrationToken = new ArrayList<String>();
        this.registrationToken.add(registrationToken);
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }
}
