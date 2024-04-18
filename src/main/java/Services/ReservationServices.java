package Services;

import Entities.Event;
import Utils.MyDatabase;
import Entities.Reservation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationServices  {
    private Connection connection;

    public ReservationServices() {
        connection = MyDatabase.getInstance().getConnection();
    }


    public void ajouter(Reservation reservation) throws SQLException {
        String req = "INSERT INTO reservation (created_at) VALUES (?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setTimestamp(1, Timestamp.valueOf(reservation.getCreatedAt()));
            stm.executeUpdate();
        }
        System.out.println("Reservation ajoutée");
    }




    public void modifier(Reservation reservation) throws SQLException {
        String req = "UPDATE reservation SET created_at = ? WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            stm.setTimestamp(1, Timestamp.valueOf(reservation.getCreatedAt()));
            stm.setInt(2, reservation.getId());
            stm.executeUpdate();
        }
        System.out.println("Reservation modifiée");
    }


    public void supprimer(Reservation reservation) throws SQLException {
        String req = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(req)) {
            // Définir l'ID de la réservation à supprimer
            stm.setInt(1, reservation.getId());
            stm.executeUpdate();
        }
        System.out.println("Reservation supprimée");
    }


    public List<Reservation> afficher() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(req)) {
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setCreatedAt(rs.getTimestamp("create_at").toLocalDateTime());
                int Event =rs.getInt("event_id");
                String User =rs.getString("user_cin");

                EventServices x = new EventServices();
                r.setEvent(x.getEventById(Event));
                UserService y = new UserService();
                r.setUser(y.getUserByCin(User));

                // Ajouter la réservation à la liste
                reservations.add(r);
            }
        }
        return reservations;
    }
}



