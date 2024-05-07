package Entities;

import java.util.Date;
import java.util.List;

public class Publication {
    private int id;
    private String titrep;
    private String typep;
    private String contentp;
    private Date datecrp;
    private String imagename;
    private Date updated;
    private String usercin;
    private List<Comment> comments;
    private String image;

    public Publication() {}

    public Publication(int id, String titrep, String typep, String contentp) {
        this.id = id;
        this.titrep = titrep;
        this.typep = typep;
        this.contentp = contentp;
    }

    public void addComment(Comment comment) {
        comment.setPublication_id(this.id);
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitrep() {
        return titrep;
    }

    public void setTitrep(String titrep) {
        this.titrep = titrep;
    }

    public String getTypep() {
        return typep;
    }

    public void setTypep(String typep) {
        this.typep = typep;
    }

    public String getContentp() {
        return contentp;
    }

    public void setContentp(String contentp) {
        this.contentp = contentp;
    }

    public Date getDatecrp() {
        return datecrp;
    }

    public String getImagename() {
        return imagename;
    }

    public String getUsercin() {
        return usercin;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setDatecrp(Date datecrp) {
        this.datecrp = datecrp;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public void setUsercin(String usercin) {
        this.usercin = usercin;
    }

    @Override
    public String toString() {
        return "Publication : " + id + " " + typep + " " + contentp + " " + titrep + " " + updated + " " + datecrp + " " + usercin + " " + imagename;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
