package Entities;

import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Programme {
    private int id;
    private String titre_pro;
    private String plan_pro;
    private int place_dispo;
    private Date date_pro;
    private String image;
    private User user;

    private List<Objectif> Objectifs;

    public Programme() {
    }

    public Programme(int id, String titre_pro, String plan_pro, int place_dispo, Date date_pro, String imagePath, User user, List<Objectif> objectifs) {
        this.id = id;
        this.titre_pro = titre_pro;
        this.plan_pro = plan_pro;
        this.place_dispo = place_dispo;
        this.date_pro = date_pro;
        this.image = imagePath;
        this.user = user;
        Objectifs = objectifs;
    }

//    public Programme(int id, String titre_pro, String plan_pro, int place_dispo, Date date_pro, String imagePath, List<Objectif> ojbectifs) {
//        this.id = id;
//        this.titre_pro = titre_pro;
//        this.plan_pro = plan_pro;
//        this.place_dispo = place_dispo;
//        this.date_pro = date_pro;
//        this.imagePath = imagePath;
//        this.Objectifs = ojbectifs;
//    }
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Programme)) return false;
    Programme programme = (Programme) o;
    return id == programme.id;
}

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public List<Objectif> getOjbectifs() {
        return this.Objectifs;
    }

    public void setObjectifs(List<Objectif> objectifs) {
        if (objectifs == null || objectifs.isEmpty()) {
            this.Objectifs = new ArrayList<>();
        } else {
            this.Objectifs = objectifs;
        }
    }


    public Programme(int id, String titrepro, String planpro, int placedispo, Date datepro, String imagePath) {
        this.id = id;
        this.titre_pro = titrepro;
        this.plan_pro = planpro;
        this.place_dispo = placedispo;
        this.date_pro = datepro;
        this.image = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitrepro() {
        return titre_pro;
    }

    public void setTitrepro(String titrepro) {
        this.titre_pro = titrepro;
    }

    public String getPlanpro() {
        return plan_pro;
    }

    public void setPlanpro(String planpro) {
        this.plan_pro = planpro;
    }

    public int getPlacedispo() {
        return place_dispo;
    }

    public void setPlacedispo(int placedispo) {
        this.place_dispo = placedispo;
    }

    public java.sql.Date getDatepro() {
        return (java.sql.Date) date_pro;
    }

    public void setDatepro(Date datepro) {
        this.date_pro = datepro;
    }

    public String getImagePath() {
        return image;
    }

    public void setImagePath(String imagePath) {
        this.image = imagePath;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +
                ", titre_pro='" + titre_pro + '\'' +
                ", plan_pro='" + plan_pro + '\'' +
                ", place_dispo=" + place_dispo +
                ", date_pro=" + date_pro +
                ", image='" + image + '\'' +
                ", Objectifs=" + Objectifs +
                '}';
    }
}