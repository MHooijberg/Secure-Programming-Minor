package devlink_server;

public class Repository {
    private int id;
    private String node_id;
    private String name;
    private GitHubUser owner;
    private Boolean isPrivate;
    private String html_url;
    private String description;
    private String title;

    public Repository() {
    }

    public Repository(int id, String node_id, String name, GitHubUser owner, Boolean isPrivate, String html_url,
            String description, String title) {
        this.id = id;
        this.node_id = node_id;
        this.name = name;
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.html_url = html_url;
        this.description = description;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GitHubUser getOwner() {
        return owner;
    }

    public void setOwner(GitHubUser owner) {
        this.owner = owner;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    


}
