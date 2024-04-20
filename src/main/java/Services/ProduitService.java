package Services;

import Utils.MyDatabase;
import Entities.Produit;
import Entities.CategorieProd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IServices<Produit> {

    private Connection connection;



    public ProduitService() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override

    public void add(Produit p) throws SQLException {
        String req = "INSERT INTO produit (name, description, image, quantity, price,categorie_prod_id) VALUES (?, ?, ?, ?, ?,?)";


        try (PreparedStatement stm = connection.prepareStatement(req)) {
            // Définition des valeurs à l'aide des méthodes setter
            stm.setString(1, p.getName());
            stm.setString(2, p.getDescription());
            stm.setString(3, p.getImage());
            stm.setInt(4, p.getQuantity());
            stm.setDouble(5, p.getPrice());
            stm.setInt(6,p.getCategorie().getId());

            // Exécution de la requête
            stm.executeUpdate();

            System.out.println("produit ajoutée.");
        } catch (SQLException e) {
            // Gestion de l'exception SQLException
            System.err.println("Erreur lors de l'ajout de la produit : " + e.getMessage());
            // Vous pouvez également propager l'exception à la couche supérieure si nécessaire
            throw e;
        }
    }

    @Override



    public void update(Produit produit) throws SQLException {


        String req="UPDATE produit set name=?,description=?,image=?,quantity=?,price=?,categorie_prod_id=? where ref=?  WHERE ref=?";


        try (PreparedStatement preparedStatement= connection.prepareStatement(req)){

        // Définition des valeurs à l'aide des méthodes setter
        preparedStatement.setString(1,produit.getName());
        preparedStatement.setString(2,produit.getDescription());
        preparedStatement.setString(3,produit.getImage());
        preparedStatement.setInt(4,produit.getQuantity());
        preparedStatement.setDouble(5,produit.getPrice());
        preparedStatement.setInt(6,produit.getCategorie().getId());
        preparedStatement.setInt(6,produit.getRef());


        preparedStatement.executeUpdate();
    }
        System.out.println("Produit modifié");
    }

    public void delete(Produit produit) throws SQLException {
        String sql = "DELETE FROM produit WHERE ref = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, produit.getRef());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting produit: " + e.getMessage());
            throw e;
        }
    }

    @Override



    public List<Produit> show() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, c.id AS category_id, c.nom_ca AS category_name FROM produit p JOIN categorie_prod c ON p.categorie_prod_id = c.id";
        try (PreparedStatement stm = connection.prepareStatement(req);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                CategorieProd categorie = new CategorieProd(rs.getInt("category_id"), rs.getString("category_name"));
                Produit produit = new Produit(
                        rs.getInt("ref"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        categorie
                );
                produits.add(produit);
            }
        }
        return produits;
    }





}

