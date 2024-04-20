package Services;
import Utils.MyDatabase;
import Entities.CategorieProd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IServices<CategorieProd>{
    private Connection connection;

    public CategorieService() {
        connection = MyDatabase.getInstance().getConnection();
    }



    @Override

    public void add(CategorieProd cat) throws SQLException {
        String req = "INSERT INTO categorie_prod (nom_cat, create_dateCat, description) VALUES(?,?,?)";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, cat.getId());
            stm.setString(2, cat.getNom_ca());
            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(cat.getCreate_date_ca().getTime());
            stm.setDate(2, sqlDate);
            stm.setString(4, cat.getDescription_cat());
            stm.executeUpdate(req);
        }
        System.out.println("categorie ajouté");
    }

    @Override
    public  void update(CategorieProd categorie) throws SQLException {
        String req=" UPDATE categorie_prod SET nom_cat=?,create_DateCat=?,description=? WHERE id=?";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, categorie.getNom_ca());
            // Convert java.util.Date to java.sql.Date
            java.sql.Date sqlDate = new java.sql.Date(categorie.getCreate_date_ca().getTime());
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.setString(3, categorie.getDescription_cat());
            preparedStatement.setInt(4, categorie.getId());
            preparedStatement.executeUpdate();

        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Category modifiée");

    }



    @Override
    public void delete(CategorieProd cat) throws SQLException {
        String req = "DELETE FROM categorie_prod WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, cat.getId()); // Use the 'id' parameter instead of 'Categorie.getId()'
            stm.executeUpdate();
        }
        System.out.println("Category supprimée");
    }



    @Override

    public List<CategorieProd> show() throws SQLException {
        List<CategorieProd> categories= new ArrayList<>();
        String req="select * from categorie_prod";
        Statement statement= connection.createStatement();

        ResultSet rs= statement.executeQuery(req);
        while (rs.next()){
            CategorieProd categorie= new CategorieProd();
            categorie.setNom_ca(rs.getString("nom_ca"));
            categorie.setCreate_date_ca(rs.getDate(3));
            categorie.setDescription_cat(rs.getString(4));
            categorie.setId_cat(rs.getInt("id"));


            categories.add(categorie);
        }


        return categories;
    }


    public List<CategorieProd> findByName(String categoryName) throws SQLException {
        List<CategorieProd> categories = new ArrayList<>();
        String req = "SELECT * FROM categorie_prod WHERE nom_ca = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, categoryName);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                CategorieProd categorie = new CategorieProd();
                categorie.setId_cat(rs.getInt("id"));
                categorie.setNom_ca(rs.getString("nom_ca"));
                categorie.setCreate_date_ca(rs.getDate("create_date_ca"));
                categorie.setDescription_cat(rs.getString("description_cat"));
                categories.add(categorie);
            }
        }
        return categories;
    }

    public CategorieProd getCategoryByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM categorie_prod WHERE nom_ca = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    CategorieProd category = new CategorieProd();
                    category.setId_cat(resultSet.getInt("id"));
                    category.setNom_ca(resultSet.getString("nom_ca"));
                    // You may need to set other properties like create_date_ca and description_cat if required
                    return category;
                }
            }
        }
        // If no category found with the given title, return null
        return null;
    }



}
