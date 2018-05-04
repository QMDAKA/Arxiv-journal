package bk.model;

public class Tag {
    public int id;
    public String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
//getter and setter methods

    public Tag(int id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }
}
