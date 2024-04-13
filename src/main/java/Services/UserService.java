package Services;

import Entities.User;
import Utils.MyDatabase;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;


public class UserService implements IServices<User> {

    private Connection connection;

    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(User user) throws SQLException {
        String sql = "INSERT INTO `user`(`email`, `roles`, `password`, `cin`, `lastname`, `firstname`, `gender`, `datebirth`, `phone`, `created_at`, `is_banned`, `profile_picture`, `is_verified`, `auth_code`) " +
                "VALUES ('" + user.getEmail() + "', '" + user.getRoles() + "', '" + user.getPassword() + "', '" + user.getCin() + "', '" + user.getLastname() + "', '" + user.getFirstname() + "', '" + user.getGender() + "', '" + user.getDatebirth() + "', '" + user.getPhone() + "', '" + user.getCreated_at() + "', " + user.getIs_banned() + ", '" + user.getProfile_picture() + "', " + user.getIs_verified() + ", '" + user.getAuth_code() + "')";
        System.out.println(sql);
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void modifier(User user) throws SQLException {
        String sql = "UPDATE `user` SET `email`= ?,`roles`=?,`password`=?,`cin`=?,`lastname`=?,`firstname`=?,`gender`=?,`datebirth`=?,`phone`=?,`created_at`=?,`is_banned`=?,`profile_picture`=?,`is_verified`=?,`auth_code`=? WHERE cin = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getRoles());
        preparedStatement.setString(3, user.getPassword());
        preparedStatement.setString(4, user.getCin());
        preparedStatement.setString(5, user.getLastname());
        preparedStatement.setString(6, user.getFirstname());
        preparedStatement.setString(7, user.getGender());
        preparedStatement.setDate(8, user.getDatebirth());
        preparedStatement.setString(9, user.getPhone());
        preparedStatement.setTimestamp(10, user.getCreated_at());
        preparedStatement.setBoolean(11, user.getIs_banned());
        preparedStatement.setString(12, user.getProfile_picture());
        preparedStatement.setBoolean(13, user.getIs_verified());
        preparedStatement.setString(14, user.getAuth_code());
        preparedStatement.setString(15, user.getCin());

        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(User user) throws SQLException {
        String sql = "delete from user where cin = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getCin());
        preparedStatement.executeUpdate();
    }

    @Override
    public List<User> afficher() throws SQLException {
        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User u = new User();
            u.setEmail(rs.getString("email"));
            u.setRoles(rs.getString("roles"));
            u.setPassword(rs.getString("password"));
            u.setCin(rs.getString("cin"));
            u.setLastname(rs.getString("lastname"));
            u.setFirstname(rs.getString("firstname"));
            u.setGender(rs.getString("gender"));
            u.setDatebirth(rs.getDate("datebirth"));
            u.setPhone(rs.getString("phone"));
            u.setCreated_at(rs.getTimestamp("created_at"));
            u.setIs_banned(rs.getBoolean("is_banned"));
            u.setProfile_picture(rs.getString("profile_picture"));
            u.setIs_verified(rs.getBoolean("is_verified"));
            u.setAuth_code(rs.getString("auth_code"));

            users.add(u);
        }
        return users;
    }

    public User getUser(String cin) throws SQLException {
        String sql = "SELECT * FROM user WHERE `cin` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, cin);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String email = resultSet.getString("email");
            String roles = resultSet.getString("roles");
            String password = resultSet.getString("password");
            String lastname = resultSet.getString("lastname");
            String firstname = resultSet.getString("firstname");
            String gender = resultSet.getString("gender");
            Date datebirth = resultSet.getDate("datebirth");
            String phone = resultSet.getString("phone");
            Timestamp created_at = resultSet.getTimestamp("created_at");
            boolean is_banned = resultSet.getBoolean("is_banned");
            String profile_picture = resultSet.getString("profile_picture");
            boolean is_verified = resultSet.getBoolean("is_verified");
            String auth_code = resultSet.getString("auth_code");

            return new User(email, roles, password, cin, lastname, firstname, gender, datebirth, phone, created_at, is_banned, profile_picture, is_verified, auth_code);
        } else {
            // Handle the case when no matching record is found
            return null;
        }

    }

    public void modifierSansMdp(User user) throws SQLException {
        String sql = "UPDATE `user` SET `email`= ?,`roles`=?,`cin`=?,`lastname`=?,`firstname`=?,`gender`=?,`datebirth`=?,`phone`=?,`created_at`=?,`is_banned`=?,`profile_picture`=?,`is_verified`=?,`auth_code`=? WHERE cin = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getRoles());
        preparedStatement.setString(3, user.getCin());
        preparedStatement.setString(4, user.getLastname());
        preparedStatement.setString(5, user.getFirstname());
        preparedStatement.setString(6, user.getGender());
        preparedStatement.setDate(7, user.getDatebirth());
        preparedStatement.setString(8, user.getPhone());
        preparedStatement.setTimestamp(9, user.getCreated_at());
        preparedStatement.setBoolean(10, user.getIs_banned());
        preparedStatement.setString(11, user.getProfile_picture());
        preparedStatement.setBoolean(12, user.getIs_verified());
        preparedStatement.setString(13, user.getAuth_code());
        preparedStatement.setString(14, user.getCin());

        preparedStatement.executeUpdate();
    }

    public List<User> afficherBack(String cin) throws SQLException {
        String sql = "SELECT cin, email, lastname, firstname, gender, is_banned, is_verified, roles  FROM user WHERE `cin` != ? ORDER BY email";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, cin);
        ResultSet rs = preparedStatement.executeQuery();

        /*Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);*/
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User u = new User();
            u.setEmail(rs.getString("email"));
            u.setRoles(rs.getString("roles"));
            u.setCin(rs.getString("cin"));
            u.setLastname(rs.getString("lastname"));
            u.setFirstname(rs.getString("firstname"));
            u.setGender(rs.getString("gender"));
            u.setIs_banned(rs.getBoolean("is_banned"));
            u.setIs_verified(rs.getBoolean("is_verified"));

            users.add(u);
        }
        return users;
    }
}
