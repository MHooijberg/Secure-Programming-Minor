package devlink_server;

public class Commit {
    private String html_url;
    private String sha;
    private String node_id;
    private GitHubUser author;
    private GitHubUser committer;
    private String message;
    public Commit() {
    }
    public Commit(String html_url, String sha, String node_id, GitHubUser author, GitHubUser committer,
            String message) {
        this.html_url = html_url;
        this.sha = sha;
        this.node_id = node_id;
        this.author = author;
        this.committer = committer;
        this.message = message;
    }
    public String getHtml_url() {
        return html_url;
    }
    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }
    public String getSha() {
        return sha;
    }
    public void setSha(String sha) {
        this.sha = sha;
    }
    public String getNode_id() {
        return node_id;
    }
    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }
    public GitHubUser getAuthor() {
        return author;
    }
    public void setAuthor(GitHubUser author) {
        this.author = author;
    }
    public GitHubUser getCommitter() {
        return committer;
    }
    public void setCommitter(GitHubUser committer) {
        this.committer = committer;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    
}
