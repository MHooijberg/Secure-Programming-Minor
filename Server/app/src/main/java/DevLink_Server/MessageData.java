package devlink_server;

import com.google.cloud.firestore.DocumentReference;

public class MessageData {
    public enum MessageType {
        Text,
        Command
    }

    private String content;
    private Long id;
    // TODO: Might want to convert this type to a custom enum.
    private MessageType type;
    private DocumentReference user;
    
    // [START firestore_data_custom_type_definition]
    public MessageData(){}

    public MessageData(String content, long id, String type, DocumentReference user){
        this.content = content;
        this.id = id;
        if (type == "Command")
            this.type = MessageType.Command;
        else if (type == "Text")
            this.type = MessageType.Text;
        this.user = user;
    }

    public MessageData(String content, long id, MessageType type, DocumentReference user){
        this.content = content;
        this.id = id;
        this.type = type;
        this.user = user;
    }
    // [END firestore_data_custom_type_definition]

    public String getContent(){ 
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public MessageType getType(){
        return type;
    }
    public void setType(String type){
        if (type == "Command")
        this.type = MessageType.Command;
        else if (type == "Text")
        this.type = MessageType.Text;
    }
    public void setType(MessageType type){
        this.type = type;
    }

    public DocumentReference getUser(){
        return user;
    }
    public void setUser(DocumentReference user){
        this.user = user;
    }
}
