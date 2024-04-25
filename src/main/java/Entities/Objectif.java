package Entities;

public class Objectif {
    private int id;
    private String objectifo;
    private String descriptiono;
    private float poido;
    private float tailleo;
    private Programme programme;



    public Objectif(int id, String objectifo, String descriptiono, float poido, float tailleo, Programme programme) {
        this.id = id;
        this.objectifo = objectifo;
        this.descriptiono = descriptiono;
        this.poido = poido;
        this.tailleo = tailleo;
        this.programme = programme;
    }

    public Objectif() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public float getTailleo() {
        return tailleo;
    }

    public void setTailleo(float tailleo) {
        this.tailleo = tailleo;
    }

    public float getPoido() {
        return poido;
    }

    public void setPoido(float poido) {
        this.poido = poido;
    }

    public String getDescriptiono() {
        return descriptiono;
    }

    public void setDescriptiono(String descriptiono) {
        this.descriptiono = descriptiono;
    }

    public String getObjectifo() {
        return objectifo;
    }

    public void setObjectifo(String objectifo) {
        this.objectifo = objectifo;
    }


}
