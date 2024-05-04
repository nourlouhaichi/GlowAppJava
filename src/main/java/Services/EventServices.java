package Services;


import Entities.Category;
import Entities.User;
import Utils.MyDatabase;
import Entities.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventServices  {
    private Connection connection;

    public EventServices() {
        connection = MyDatabase.getInstance().getConnection();
    }


    public void ajouter(Event event) throws SQLException {
        String req = "INSERT INTO event (title, description, location, date, nb_p, user_cin, category_id, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setString(1, event.getTitle());
            stm.setString(2, event.getDescription());
            stm.setString(3, event.getLocation());
            stm.setTimestamp(4, Timestamp.valueOf(event.getDate()));
            stm.setInt(5, event.getNbP());
            stm.setInt(6, Integer.parseInt(event.getUserId().getCin()));
            stm.setInt(7, event.getCategoryId().getId());
            stm.setString(8, event.getImageUrl()); // Set the image URL
            stm.executeUpdate();
        }
        System.out.println("Event ajouté");
    }


    public void modifier(Event event) throws SQLException {
        String req = "UPDATE event SET title = ?, description = ?, location = ?, date = ?, nb_p = ?,image = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setString(1, event.getTitle());
            stm.setString(2, event.getDescription());
            stm.setString(3, event.getLocation());
            stm.setTimestamp(4, Timestamp.valueOf(event.getDate()));
            stm.setInt(5, event.getNbP());
            stm.setString(6, event.getImageUrl());
            stm.setInt(7, event.getCategoryId().getId());
            stm.setInt(8, event.getId());

            stm.executeUpdate();
        }
        System.out.println("Event modifié");
    }
    public Event getEventById(int eventId) throws SQLException {
        Event event = null;
        String req = "SELECT e.*, u.firstname, u.lastname FROM event e JOIN user u ON e.user_cin = u.cin WHERE e.id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, eventId);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    event = new Event();
                    event.setId(rs.getInt("id"));
                    event.setTitle(rs.getString("title"));
                    event.setDescription(rs.getString("description"));
                    event.setLocation(rs.getString("location"));
                    event.setImageUrl(rs.getString("image"));
                    event.setDate(rs.getTimestamp("date").toLocalDateTime());
                    event.setNbP(rs.getInt("nb_p"));
                    String cin = String.valueOf(rs.getInt("user_cin"));
                    int categoryId = rs.getInt("category_id");
                    User user= new User();
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    CategoryServices categoryServices = new CategoryServices();
                    Category category = categoryServices.getCategoryById(categoryId);

                    event.setUserId(user);
                    event.setCategoryId(category);
                }
            }
        }
        return event;
    }





    public void supprimer(Event event) throws SQLException {
        String req = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setInt(1, event.getId());
            stm.executeUpdate();
        }
        System.out.println("Event supprimé");
    }


    public List<Event> afficher() throws SQLException {
        List<Event> events = new ArrayList<>();
        String req = "SELECT e.*, u.firstname, u.lastname FROM event e JOIN user u ON e.user_cin = u.cin";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Event e = new Event();
                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setDescription(rs.getString("description"));
                e.setLocation(rs.getString("location"));
                e.setDate(rs.getTimestamp("date").toLocalDateTime()); // Convertir Timestamp en LocalDateTime
                e.setNbP(rs.getInt("nb_p"));
                e.setImageUrl(rs.getString("image"));
                System.out.println(e.getImageUrl());
                // Retrieve the cin from the result set
                String cin = String.valueOf(rs.getInt("user_cin"));
                int id_cat = (rs.getInt("category_id"));
                UserService userService = new UserService();
                CategoryServices catus = new CategoryServices();
                Category cat = catus.getCategoryById(id_cat);
                // Retrieve the User object associated with the cin
                User user = userService.getUser(cin);
                // Check if the User object is null
                e.setUserId(user);
                e.setCategoryId(cat);

                events.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return events;
    }
}



