package Entities;

import java.util.*;
import java.util.Date;
import java.util.List;


public class Programme {
    private int id;
    private String titre_pro;
    private String plan_pro;
    private int place_dispo;
    private Date date_pro;
    private String imagePath;

    private List<Objectif> Objectifs;

    public Programme() {
    }

    public Programme(int id, String titre_pro, String plan_pro, int place_dispo, Date date_pro, String imagePath, List<Objectif> ojbectifs) {
        this.id = id;
        this.titre_pro = titre_pro;
        this.plan_pro = plan_pro;
        this.place_dispo = place_dispo;
        this.date_pro = date_pro;
        this.imagePath = imagePath;
        this.Objectifs = ojbectifs;
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
        this.imagePath = imagePath;
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
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Programme{" +
                "id=" + id +
                ", titre_pro='" + titre_pro + '\'' +
                ", plan_pro='" + plan_pro + '\'' +
                ", place_dispo=" + place_dispo +
                ", date_pro=" + date_pro +
                ", imagePath='" + imagePath + '\'' +
                ", Objectifs=" + Objectifs +
                '}';
    }
}