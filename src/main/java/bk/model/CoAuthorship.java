package bk.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by quangminh on 18/11/2017.
 */
@Entity
public class CoAuthorship {
    @Id
    @GeneratedValue
    Long id;

    String authorId1;
    String authorId2;
    String paperId;
    int year;
    boolean flag = false;

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

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public CoAuthorship() {
        this.flag = false;
    }
}
