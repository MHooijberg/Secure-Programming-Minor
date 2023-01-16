package devlink_server;

import com.google.cloud.firestore.DocumentReference;

public class MessageData {
    private String content;
    private Long id;
    private String type;
    private DocumentReference user;
    
    // [START firestore_data_custom_type_definition]
    public MessageData(){}

    public MessageData(String content, Long id, String type, DocumentReference user) {
        this.content = content;
        this.id = id;
        this.type = type;
        this.user = user;
    }
    // [END firestore_data_custom_type_definition]

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public DocumentReference getUser() {
        return user;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(DocumentReference user) {
        this.user = user;
    }
    // public String getContent(){ 
    //     return content;
    // }
    // public void setContent(String content){
    //     this.content = content;
    // }

    // public long getId(){
    //     return id;
    // }
    // public void setId(long id){
    //     this.id = id;
    // }
    // public String getType(){
    //     return type;
    // }
    // public void setType(String type){
    //     this.type = type;
    // }

    // public DocumentReference getUser(){
    //     return user;
    // }
    // public void setUser(DocumentReference user){
    //     this.user = user;
    // }
}
