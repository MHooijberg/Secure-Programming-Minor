package devlink_server;

public class ShallowUser {
    private Integer accept_rate;
    private Integer account_id;
    private String display_name;
    private String link;
    private String profile_image;
    private Integer reputation;
    private Integer user_id;
    private String user_type;

    public ShallowUser() {}

    public ShallowUser(Integer accept_rate, Integer account_id, String display_name, String link, String profile_image,
            Integer reputation, Integer user_id, String user_type) {
        this.accept_rate = accept_rate;
        this.account_id = account_id;
        this.display_name = display_name;
        this.link = link;
        this.profile_image = profile_image;
        this.reputation = reputation;
        this.user_id = user_id;
        this.user_type = user_type;
    }

    public Integer getAccept_rate() {
        return accept_rate;
    }
    public void setAccept_rate(Integer accept_rate) {
        this.accept_rate = accept_rate;
    }
    public Integer getAccount_id() {
        return account_id;
    }
    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }
    public String getDisplay_name() {
        return display_name;
    }
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getProfile_image() {
        return profile_image;
    }
    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
    public Integer getReputation() {
        return reputation;
    }
    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public String getUser_type() {
        return user_type;
    }
    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    
}
