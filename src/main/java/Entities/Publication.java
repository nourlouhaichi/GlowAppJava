package Entities;

import java.util.*;

public class Publication {
    private int id;
    private  String titrep;
    private  String typep;
    private  String contentp;
    private Date datecrp;
    private String imagename;
    private Date updated;
    private String usercin;

    public Publication(){

    }


    public Publication(int id, String titrep, String typep, String contentp ) {
        this.id = id;
        this.titrep = titrep;
        this.typep = typep;
        this.contentp = contentp;
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String gettitrep() {
        return titrep;
    }

    public void settitrep(String titrep) {
        this.titrep = titrep;
    }

    public String gettypep() {
        return typep;
    }

    public void settypep(String typep) {
        this.typep = typep;
    }

    public String getcontentp() {
        return contentp;
    }

    public void setcontent(String typep) {
        this.typep = typep;
    }

    public Date getDatecrp() {
        return datecrp;
    }

    public String getImagename() {return imagename;}

    public String getUsercin(){return usercin;}

    public Date getUpdated(){return  updated;}

    public void setDatecrp(Date datecrp){this.datecrp=datecrp;}

    public void setUpdated(Date updated){this.updated=updated;}

    public void setImagename(String imagename){this.imagename=imagename;}

    public void  setUsercin(String usercin){this.usercin=usercin;}
    @Override
    public String toString() {
        return "Publication : "+id+" "+typep+" "+contentp+" "+titrep+" "+updated+" "+datecrp+" "+usercin+" "+imagename;
    }
}