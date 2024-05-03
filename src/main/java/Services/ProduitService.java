package Services;

import Entities.User;
import Utils.MyDatabase;
import Entities.Produit;
import Entities.CategorieProd;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProduitService implements IServices<Produit> {

    private Connection connection;



    public ProduitService() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override

    public void add(Produit p) throws SQLException {
        String req = "INSERT INTO produit (name, description, image, quantity, price,categorie_prod_id, user_cin) VALUES (?, ?, ?, ?, ?,?,?)";


        try (PreparedStatement stm = connection.prepareStatement(req)) {
            // Définition des valeurs à l'aide des méthodes setter
            stm.setString(1, p.getName());
            stm.setString(2, p.getDescription());
            stm.setString(3, p.getImage());
            stm.setInt(4, p.getQuantity());
            stm.setDouble(5, p.getPrice());
            stm.setInt(6,p.getCategorie().getId());
            stm.setInt(7, Integer.parseInt(p.getUser().getCin()));

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
        StringBuilder reqBuilder = new StringBuilder("UPDATE produit SET name=?, description=?, quantity=?, price=?, categorie_prod_id=?");
        List<Object> values = new ArrayList<>(Arrays.asList(produit.getName(), produit.getDescription(), produit.getQuantity(), produit.getPrice(), produit.getCategorie().getId()));

        // Check if an image path is provided
        String imagePath = produit.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            reqBuilder.append(", image=?"); // Add the image column to the query
            values.add(imagePath); // Add the image path to the values list
        }

        reqBuilder.append(" WHERE ref=?"); // Add the WHERE clause
        values.add(produit.getRef()); // Add the product reference to the values list

        String req = reqBuilder.toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            // Set the values in the prepared statement
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i + 1, values.get(i));
            }

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
        String req = "SELECT p.*, u.firstname, u.lastname, c.id AS category_id, c.nom_ca AS category_name " +
                "FROM produit p " +
                "JOIN user u ON p.user_cin = u.cin " +
                "JOIN categorie_prod c ON p.categorie_prod_id = c.id";
        try (PreparedStatement stm = connection.prepareStatement(req);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                CategorieProd categorie = new CategorieProd(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User();
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                Produit produit = new Produit(
                        rs.getInt("ref"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        categorie,
                        user
                );
                produits.add(produit);
            }
        }
        return produits;
    }


    @Override
    public Produit afficher(Produit produit) throws SQLException {
        return null;
    }

    @Override
    public void ajouter(Produit produit) throws SQLException {

    }

    @Override
    public void modifier(Produit produit) throws SQLException {

    }

    @Override
    public void supprimer(Produit produit) throws SQLException {

    }

    @Override
    public List<Produit> afficher() throws SQLException {
        return null;
    }


    public List<Produit> getAllProduits() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT p.*, u.firstname, u.lastname, c.id AS category_id, c.nom_ca AS category_name " +
                "FROM produit p " +
                "JOIN categorie_prod c ON p.categorie_prod_id = c.id " +
                "JOIN user u ON p.user_cin = u.cin";
        try (PreparedStatement stm = connection.prepareStatement(req);
             ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                CategorieProd categorie = new CategorieProd(rs.getInt("category_id"), rs.getString("category_name"));
                User user = new User();
                user.setFirstname(rs.getString("firstname"));
                user.setLastname(rs.getString("lastname"));
                Produit produit = new Produit(
                        rs.getInt("ref"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),

                        categorie,
                        user
                );
                produits.add(produit);
            }
        }
        return produits;
    }



    public Produit getProduitById(int produitId) throws SQLException {
        Produit produit = null;
        String req = "SELECT p.*, c.nom_ca FROM produit p JOIN categorie_prod c ON p.categorie_prod_id = c.id WHERE p.ref = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, produitId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    produit = new Produit();
                    produit.setRef(rs.getInt("ref"));
                    produit.setName(rs.getString("name"));
                    produit.setDescription(rs.getString("description"));
                    produit.setImage(rs.getString("image"));
                    produit.setQuantity(rs.getInt("quantity"));
                    produit.setPrice(rs.getDouble("price"));
                    int categoryId = rs.getInt("categorie_prod_id");
                    CategorieService categoryServices = new CategorieService();
                    CategorieProd categorie = categoryServices.getCategorieById(categoryId);
                    produit.setCategorie(categorie);
                }
            }
        }
        return produit;
    }


}