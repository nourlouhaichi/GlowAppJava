package Entities;


import java.util.Date;

public class CategorieProd {
    private int id ;

    private String nom_ca;

    private Date create_date_ca;

    private  String description_cat;

    public CategorieProd() {
    }
    public CategorieProd(int id, String nom_ca, Date create_date_ca, String description_cat) {
        this.id=id;
        this.nom_ca=nom_ca ;
        this.create_date_ca=create_date_ca;
        this.description_cat=description_cat;
    }
    public CategorieProd(int id, String nom_ca) {
        this.id = id;
        this.nom_ca = nom_ca;
    }
    public CategorieProd(String nom_ca, Date create_date_ca, String description_cat) {

        this.nom_ca=nom_ca ;
        this.create_date_ca=create_date_ca;
        this.description_cat=description_cat;
    }

    public int getId() {
        return id;
    }

    public void setId_cat(int id) {
        this.id = id;
    }

    public String getNom_ca() {
        return nom_ca;
    }

    public Date getCreate_date_ca() {
        return create_date_ca;
    }

    public void setCreate_date_ca(Date create_date_ca) {
        this.create_date_ca = create_date_ca;
    }

    public void setNom_ca(String nom_ca) {
        this.nom_ca = nom_ca;
    }


    public String getDescription_cat() {
        return description_cat;
    }

    public void setDescription_cat(String description_cat) {
        this.description_cat = description_cat;
    }

    @Override
    public String toString() {
        return "categorie{" +
                "id=" + id +
                ", nom_ca='" + nom_ca + '\'' +
                ", create_date_ca='" + create_date_ca + '\'' +
                ", description_cat='" + description_cat + '\'' +
                '}';
    }
}

