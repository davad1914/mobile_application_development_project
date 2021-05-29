package hu.david.mobileproject;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private String id;
    private String userId;
    private String title;
    private String desc;
    private Date createDate;

    public Note() {}

    public Note(String title, String desc, Date createDate) {
        this.userId = "valami";
        this.title = title;
        this.desc = desc;
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String _getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}