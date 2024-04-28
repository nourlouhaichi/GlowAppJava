package Entities;

public class Comment {
    private int id;
    private Publication publication;
    private String contenue;

    public Comment(int id, int publication_id, String contenue) {
        this.id = id;
        this.publication = new Publication();
        this.publication.setId(publication_id);
        this.contenue = contenue;
    }

    public Comment() {
        this.publication = new Publication();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPublication_id() {
        return publication.getId();
    }

    public void setPublication_id(int publication_id) {
        this.publication.setId(publication_id);
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }
}
