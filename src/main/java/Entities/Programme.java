package Entities;

import java.util.*;

public class Programme {
    private int id;
    private  String titre_pro;
    private  String plan_pro;
    private  int place_dispo;
    private Date date_pro;

    public Programme(){

    }

    public Programme(int id, String titrepro, String planpro, int placedispo, Date datepro) {
        this.id = id;
        this.titre_pro = titrepro;
        this.plan_pro = planpro;
        this.place_dispo = placedispo;
        this.date_pro = datepro;
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


    @Override
    public String toString() {
            return "Programme{" +
                    "id=" + id +
                    ", titrepro='" + titre_pro + '\'' +
                    ", planpro='" + plan_pro + '\'' +
                    ", placedispo=" + place_dispo +
                    ", datepro=" + date_pro +

                    '}';
    }
}