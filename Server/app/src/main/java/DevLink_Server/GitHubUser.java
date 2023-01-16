package devlink_server;

public class GitHubUser {
    private int id;
    private String node_id;
    private String html_url;
    private String name;
    private String company;
    private String location;
    private String email;
    private String bio;
    private String blog;
    public GitHubUser() {
    }
    public GitHubUser(int id, String node_id, String html_url, String name, String company, String location,
            String email, String bio, String blog) {
        this.id = id;
        this.node_id = node_id;
        this.html_url = html_url;
        this.name = name;
        this.company = company;
        this.location = location;
        this.email = email;
        this.bio = bio;
        this.blog = blog;
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
    public String getHtml_url() {
        return html_url;
    }
    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getBlog() {
        return blog;
    }
    public void setBlog(String blog) {
        this.blog = blog;
    }

    
}
