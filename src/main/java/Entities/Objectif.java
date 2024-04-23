package Entities;


public class Objectif {
    private int id;
    private  String objectifo;
    private  String descriptiono;
    private  float poido;
    private float tailleo;
    private String usercin;



    public Objectif(){

    }

    public Objectif(int id, String objectifo, String descriptiono, float poido, float tailleo, String usercin) {
        this.id = id;
        this.objectifo = objectifo;
        this.descriptiono = descriptiono;
        this.poido = poido;
        this.tailleo = tailleo;
        this.usercin = usercin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectifo() {
        return objectifo;
    }

    public void setObjectifo(String objectifo) {
        this.objectifo = objectifo;
    }

    public String getDescriptiono() {
        return descriptiono;
    }

    public void setDescriptiono(String descriptiono) {
        this.descriptiono = descriptiono;
    }

    public float getPoido() {
        return poido;
    }

    public void setPoido(float poido) {
        this.poido = poido;
    }

    public float getTailleo() {
        return tailleo;
    }

    public void setTailleo(float tailleo) {
        this.tailleo = tailleo;
    }

    public String getUsercin() {
        return usercin;
    }

    public void setUsercin(String usercin) {
        this.usercin = usercin;
    }

    @Override
    public String toString() {
        return "Objectif{" +
                "id=" + id +
                ", objectifo='" + objectifo + '\'' +
                ", descriptiono='" + descriptiono + '\'' +
                ", poido=" + poido +
                ", tailleo=" + tailleo +
                ", usercin='" + usercin + '\'' +
                '}';
    }
}


