package Entities;

public class Produit {
    private int ref;

    private String name ,description,image ;

    private int quantity ;
    private double price ;

    private CategorieProd categorie; // Add a Category field

    public Produit() {
    }

    public Produit(int ref , String  name, String description , String image , int quantity , double price , CategorieProd categorie  ) {
        this.ref=ref ;
        this.name = name;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.price=price ;
        this.categorie = categorie; // Initialize the category

    }
    public Produit( String  name, String description , String image , int quantity , double price , CategorieProd categorie  ) {

        this.name = name;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.price=price ;
        this.categorie = categorie; // Initialize the category

    }
    public Produit(String  name, double price) {
        this.name = name;
        this.price=price ;

    }

    public int getRef() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public CategorieProd getCategorie() {
        return categorie;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }


    public void setName(String name) {
        this.name= name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice( double price) {
        this.price = price;
    }

    public void setCategorie(CategorieProd categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "produit{" +
                "ref=" + ref +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}


