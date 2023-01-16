package devlink_server;

//import java.util.Date;

import com.google.type.Date;

public class Question {
    private String[] tags;
    private ShallowUser owner;
    private Boolean is_answered;
    private Integer view_count;
    private Integer favorite_count;
    private Integer up_vote_count;
    private Integer accepted_answer_id;
    private Integer answer_count;
    private Integer score;
    private transient Date last_activity_date;
    private transient Date creation_date;
    private transient Date last_edit_date;
    private Integer question_id;
    private String link;
    private String title;
    private String body;

    public Question() {}

    public Question(String[] tags, ShallowUser owner, Boolean is_answered, Integer view_count, Integer favorite_count,
            Integer up_vote_count, Integer accepted_answer_id, Integer answer_count, Integer score,
            Date last_activity_date, Date creation_date, Date last_edit_date, Integer question_id, String link,
            String title, String body) {
        this.tags = tags;
        this.owner = owner;
        this.is_answered = is_answered;
        this.view_count = view_count;
        this.favorite_count = favorite_count;
        this.up_vote_count = up_vote_count;
        this.accepted_answer_id = accepted_answer_id;
        this.answer_count = answer_count;
        this.score = score;
        this.last_activity_date = last_activity_date;
        this.creation_date = creation_date;
        this.last_edit_date = last_edit_date;
        this.question_id = question_id;
        this.link = link;
        this.title = title;
        this.body = body;
    }

    public String[] getTags() {
        return tags;
    }
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    public ShallowUser getOwner() {
        return owner;
    }
    public void setOwner(ShallowUser owner) {
        this.owner = owner;
    }
    public Boolean getIs_answered() {
        return is_answered;
    }
    public void setIs_answered(Boolean is_answered) {
        this.is_answered = is_answered;
    }
    public Integer getView_count() {
        return view_count;
    }
    public void setView_count(Integer view_count) {
        this.view_count = view_count;
    }
    public Integer getFavorite_count() {
        return favorite_count;
    }
    public void setFavorite_count(Integer favorite_count) {
        this.favorite_count = favorite_count;
    }
    public Integer getUp_vote_count() {
        return up_vote_count;
    }
    public void setUp_vote_count(Integer up_vote_count) {
        this.up_vote_count = up_vote_count;
    }
    public Integer getAccepted_answer_id() {
        return accepted_answer_id;
    }
    public void setAccepted_answer_id(Integer accepted_answer_id) {
        this.accepted_answer_id = accepted_answer_id;
    }
    public Integer getAnswer_count() {
        return answer_count;
    }
    public void setAnswer_count(Integer answer_count) {
        this.answer_count = answer_count;
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
