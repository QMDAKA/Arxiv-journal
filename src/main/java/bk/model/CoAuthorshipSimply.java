package bk.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CoAuthorshipSimply {
    @Id
    @GeneratedValue
    Long id;

    String authorId1;
    String authorId2;
    int coAuthorshipTime;
    int lastYear;
    boolean flag = false;

    public CoAuthorshipSimply() {
        this.flag = false;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorId1() {
        return authorId1;
    }

    public void setAuthorId1(String authorId1) {
        this.authorId1 = authorId1;
    }

    public String getAuthorId2() {
        return authorId2;
    }

    public void setAuthorId2(String authorId2) {
        this.authorId2 = authorId2;
    }

    public int getCoAuthorshipTime() {
        return coAuthorshipTime;
    }

    public void setCoAuthorshipTime(int coAuthorshipTime) {
        this.coAuthorshipTime = coAuthorshipTime;
    }

    public int getLastYear() {
        return lastYear;
    }

    public void setLastYear(int lastYear) {
        this.lastYear = lastYear;
    }
}
