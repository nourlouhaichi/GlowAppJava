package Services;

import Entities.User;
import Utils.MyDatabase;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class UserService implements IServices<User> {

    private Connection connection;

    public UserService() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(User user) throws SQLException {

    }

    @Override
    public void update(User user) throws SQLException {

    }

    @Override
    public void delete(User user) throws SQLException {

    }

    @Override
    public List<User> show() throws SQLException {
        return null;
    }

    @Override
    public User afficher(User user) throws SQLException {
        return null;
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
        String sql = "SELECT cin, email, lastname, firstname, gender, is_verified, roles  FROM user WHERE `cin` != ? ORDER BY email";

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
            u.setIs_verified(rs.getBoolean("is_verified"));

            users.add(u);
        }
        return users;
    }

    public void banUser(String cin) throws SQLException {
        String sql = "UPDATE `user` SET `is_banned`=? WHERE cin = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, true);
        preparedStatement.setString(2, cin);

        preparedStatement.executeUpdate();
    }

    public void unbanUser(String cin) throws SQLException {
        String sql = "UPDATE `user` SET `is_banned`=? WHERE cin = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, false);
        preparedStatement.setString(2, cin);

        preparedStatement.executeUpdate();
    }

    public void resetPassword(String email, String password) throws SQLException {
        String sql = "UPDATE `user` SET `password`=? WHERE email = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, password);
        preparedStatement.setString(2, email);

        preparedStatement.executeUpdate();
    }

    public void verifyUser(String cin , String code) throws SQLException {
        String sql = "UPDATE `user` SET `is_verified`=? ,`auth_code`=? WHERE cin = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, true);
        preparedStatement.setString(2, code);
        preparedStatement.setString(3, cin);

        preparedStatement.executeUpdate();
    }

    public void modifierBack(User user) throws SQLException {
        String sql = "UPDATE `user` SET `roles` = ? WHERE cin = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getRoles());
        preparedStatement.setString(2, user.getCin());

        preparedStatement.executeUpdate();
    }

    public int countGender(String gender) throws SQLException {
        String sql = "SELECT COUNT(*) AS gender_count FROM user WHERE gender = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, gender);
        ResultSet rs = preparedStatement.executeQuery();

        int genderCount = 0;
        if (rs.next()) {
            genderCount = rs.getInt("gender_count");
        }

        return genderCount;
    }

    public int countBans(Boolean ban) throws SQLException {
        String sql = "SELECT COUNT(*) AS ban_count FROM user WHERE is_banned = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setBoolean(1, ban);
        ResultSet rs = preparedStatement.executeQuery();

        int banCount = 0;
        if (rs.next()) {
            banCount = rs.getInt("ban_count");
        }

        return banCount;
    }

    public int countRole(String role) throws SQLException {
        String sql = "SELECT COUNT(*) AS role_count FROM user WHERE roles = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, role);
        ResultSet rs = preparedStatement.executeQuery();

        int roleCount = 0;
        if (rs.next()) {
            roleCount = rs.getInt("role_count");
        }

        return roleCount;
    }

    public Map<String, Integer> getUserEvolutionData() throws SQLException {
        String sql = "SELECT SUBSTRING(created_at, 1, 10) AS date, COUNT(cin) AS userCount " +
                "FROM user " +
                "GROUP BY SUBSTRING(created_at, 1, 10) " +
                "ORDER BY SUBSTRING(created_at, 1, 10) ASC";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        Map<String, Integer> userEvolutionDataMap = new HashMap<>();
        while (resultSet.next()) {
            String date = resultSet.getString("date");
            int userCount = resultSet.getInt("userCount");
            userEvolutionDataMap.put(date, userCount);
        }
        return userEvolutionDataMap;
    }

    @Override
    public void supprimer(int id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'supprimer'");
    }

}