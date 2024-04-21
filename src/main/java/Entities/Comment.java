package Entities;

public class Comment {
    private int id;
    private int publication_id;
    private String contenue;

    public Comment(int id, int publication_id, String contenue) {
        this.id = id;
        this.publication_id = publication_id;
        this.contenue = contenue;
    }
    public Comment(){

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPublication_id() {
        return publication_id;
    }
    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }
    public String getContenue() {
        return contenue;
    }
    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

}
