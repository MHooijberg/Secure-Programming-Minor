package devlink_server;

import java.util.ArrayList;
import java.util.List;

public class FCMMessageData {
    public enum ObjectType {
        CHANNEL,
        GUILD,
        MESSAGE,
        USER
    }

    private boolean hasNotification;
    private String notificationBody;
    private String notificationColor;
    private String notificationIcon;
    private String notificationTitle;
    private String objectId;
    private ObjectType objectType;
    private List<String> registrationTokens;

    public FCMMessageData(){}

    public FCMMessageData(boolean hasNotification, String notificationBody, String notificationColor,
        String notificationIcon, String notificationTitle, String objectId, ObjectType objectType,
        List<String> registrationTokens) {
            this.hasNotification = hasNotification;
            this.notificationBody = notificationBody;
            this.notificationColor = notificationColor;
            this.notificationIcon = notificationIcon;
            this.notificationTitle = notificationTitle;
            this.objectId = objectId;
            this.objectType = objectType;
            this.registrationTokens = registrationTokens;
    }
    public FCMMessageData(boolean hasNotification, String notificationBody, String notificationColor,
    String notificationIcon, String notificationTitle, String objectId, ObjectType objectType,
    String registrationTokens) {
        this.hasNotification = hasNotification;
        this.notificationBody = notificationBody;
        this.notificationColor = notificationColor;
        this.notificationIcon = notificationIcon;
        this.notificationTitle = notificationTitle;
        this.objectId = objectId;
        this.objectType = objectType;
        this.registrationTokens = new ArrayList<String>();
        this.registrationTokens.add(registrationTokens);
}

    public boolean getHasNotification()
    {
        return hasNotification;
    }
    public void setHasNotification(boolean hasNotification)
    {
        this.hasNotification = hasNotification;
    }
    public String getNotificationBody()
    {
        return notificationBody;
    }
    public void setNotificationBody(String notificationBody)
    {
        this.notificationBody = notificationBody;
    }
    public String getNotificationColor()
    {
        return notificationColor;
    }
    public void setNotificationColor(String notificationColor)
    {
        this.notificationColor = notificationColor;
    }
    public String getNotificationIcon()
    {
        return notificationIcon;
    }
    public void setNotificationIcon(String notificationIcon)
    {
        this.notificationIcon = notificationIcon;
    }
    public String getNotificationTitle()
    {
        return notificationTitle;
    }
    public void setNotificationTitle(String notificationTitle)
    {
        this.notificationTitle = notificationTitle;
    }
    public String getObjectId()
    {
        return objectId;
    }
    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }
    public ObjectType getObjectType()
    {
        return objectType;
    }
    public void setObjectType(ObjectType objectType)
    {
        this.objectType = objectType;
    }
    public List<String> getRegistrationTokens()
    {
        return registrationTokens;
    }
    public void setRegistrationTokens(List<String> registrationTokens)
    {
        this.registrationTokens = registrationTokens;
    }
    public void setRegistrationTokens(String registrationToken)
    {
        this.registrationTokens = new ArrayList<String>();
        registrationTokens.add(registrationToken);
    }
}
