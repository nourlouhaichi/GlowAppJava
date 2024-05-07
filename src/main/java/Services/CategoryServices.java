package Services;

import Services.IServices;
import Utils.MyDatabase;
import Entities.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServices  {
    private Connection connection;

    public CategoryServices() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Category category) throws SQLException {
        String req = "INSERT INTO category (id, title) VALUES (?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, category.getId());
            stm.setString(2, category.getTitle());
            stm.executeUpdate();
        }
        System.out.println("Category ajoutée");
    }

    public void modifier(Category category) throws SQLException {
        String req = "UPDATE category SET title = ? WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setString(1, category.getTitle());
            stm.setInt(2, category.getId());
            stm.executeUpdate();
        }
        System.out.println("Category modifiée");
    }

    public void supprimer(Category category) throws SQLException {
        String req = "DELETE FROM category WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, category.getId());
            stm.executeUpdate();
        }
        System.out.println("Category supprimée");
    }

    public List<Category> afficher() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String req = "SELECT * FROM category";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setTitle(rs.getString("title"));
                categories.add(c);
            }
        }
        return categories;
    }
    public Category getCategoryById(int categoryId) throws SQLException {
        Category category = null;
        String sql = "SELECT * FROM category WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setTitle(resultSet.getString("title"));
            }
        }

        return category;
    }
    public Category getCategoryByTitle(String title) throws SQLException {
        String sql = "SELECT * FROM category WHERE title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = new Category();
                    category.setId(resultSet.getInt("id"));
                    category.setTitle(resultSet.getString("title"));
                    return category;
                }
            }
        }
        // If no category found with the given title, return null
        return null;
    }
}
