package Entities;


public class Objectif {
    private int id;
    private String objectif_o;
    private String description_o;
    private Float poid_o;
    private Float taille_o;
    private int programme_id;
    private User user;

    private Programme programme;

    public Objectif(int id, String objectif_o, String description_o, Float poid_o, Float taille_o, int programme_id) {
        this.id = id;
        this.objectif_o = objectif_o;
        this.description_o = description_o;
        this.poid_o = poid_o;
        this.taille_o = taille_o;
        this.programme_id = programme_id;
    }

    public Objectif(int id, String objectif_o, String description_o, Float poid_o, Float taille_o, int programme_id, User user ) {
        this.id = id;
        this.objectif_o = objectif_o;
        this.description_o = description_o;
        this.poid_o = poid_o;
        this.taille_o = taille_o;
        this.programme_id = programme_id;
        this.user = user;
    }


    public Objectif() {
        this.programme = new Programme();
    }

    public Objectif(int id, String objectifo, String descriptiono, Float poido, Float tailleo) {
        this.id = id;
        this.objectif_o = objectifo;
        this.description_o = descriptiono;
        this.poid_o = poido;
        this.taille_o = tailleo;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getProgramme_id() {
        return programme_id;
    }

    public void setProgramme_id(int programme_id) {
        this.programme_id = programme_id;
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
                ", objectif_o='" + objectif_o + '\'' +
                ", description_o='" + description_o + '\'' +
                ", poid_o=" + poid_o +
                ", taille_o=" + taille_o +
                ", programme_id=" + programme_id +
                ", user=" + user +
                ", programme=" + programme +
                '}';
    }
}
