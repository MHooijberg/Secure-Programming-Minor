package devlink_server;

import com.google.type.Date;

public class Answer {
    private ShallowUser owner;
    private Integer down_vote_count;
    private Integer up_vote_count;
    private Boolean is_accepted;
    private Integer score;
    private transient Date last_activity_date;
    private transient Date creation_date;
    private transient Date last_edit_date;
    private Integer answer_id;
    private Integer question_id;
    private String link;
    private String title;
    private String body;

    public Answer() {
    }

    public Answer(ShallowUser owner, Integer down_vote_count, Integer up_vote_count, Boolean is_accepted, Integer score,
            Date last_activity_date, Date creation_date, Date last_edit_date, Integer answer_id, Integer question_id,
            String link, String title, String body) {
        this.owner = owner;
        this.down_vote_count = down_vote_count;
        this.up_vote_count = up_vote_count;
        this.is_accepted = is_accepted;
        this.score = score;
        this.last_activity_date = last_activity_date;
        this.creation_date = creation_date;
        this.last_edit_date = last_edit_date;
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.link = link;
        this.title = title;
        this.body = body;
    }

    public ShallowUser getOwner() {
        return owner;
    }

    public void setOwner(ShallowUser owner) {
        this.owner = owner;
    }

    public Integer getDown_vote_count() {
        return down_vote_count;
    }

    public void setDown_vote_count(Integer down_vote_count) {
        this.down_vote_count = down_vote_count;
    }

    public Integer getUp_vote_count() {
        return up_vote_count;
    }

    public void setUp_vote_count(Integer up_vote_count) {
        this.up_vote_count = up_vote_count;
    }

    public Boolean getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(Boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getLast_activity_date() {
        return last_activity_date;
    }

    public void setLast_activity_date(Date last_activity_date) {
        this.last_activity_date = last_activity_date;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getLast_edit_date() {
        return last_edit_date;
    }

    public void setLast_edit_date(Date last_edit_date) {
        this.last_edit_date = last_edit_date;
    }

    public Integer getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Integer answer_id) {
        this.answer_id = answer_id;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
