package Entities;


public class Objectif {
    private int id;
    private String objectif_o;
    private String description_o;
    private Float poid_o;
    private Float taille_o;
    private Programme programme;

    public Objectif(int id, String objectifo, String descriptiono, Float poido, Float tailleo) {
        this.id = id;
        this.objectif_o = objectifo;
        this.description_o = descriptiono;
        this.poid_o = poido;
        this.taille_o = tailleo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getObjectifO() {return objectif_o;}

    public void setObjectifO(String objectifo) {
        this.objectif_o = objectifo;
    }

    public String getDescriptionO() {return description_o;
    }

    public void setDescriptionO(String descriptiono) {
        this.description_o = descriptiono;
    }

    public Float getPoidO() {
        return poid_o;
    }

    public void setPoidO(Float poido) {
        this.poid_o = poido;
    }

    public Float getTailleO() {
        return taille_o;
    }

    public void setTailleO(Float tailleo) {
        this.taille_o = tailleo;
    }



    @Override
    public String toString() {
        return "Objectif{" +
                "id=" + id +
                ", objectifo='" + objectif_o + '\'' +
                ", descriptiono='" + description_o + '\'' +
                ", poido=" + poid_o +
                ", tailleo=" + taille_o +
                '}';
    }
}
