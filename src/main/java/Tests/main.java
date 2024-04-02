package Tests;

import java.sql.Date;
import java.sql.Timestamp;
import Services.UserService;
import java.sql.SQLException;

import Utils.MyDatabase;
import Entities.User;

public class main extends Object {

    public static void main(String[] args) {

        MyDatabase bd = MyDatabase.getInstance();
        UserService us = new UserService();

        User user = new User(
                "test@gmail.com",
                "Admin",
                "azerty",
                "12345678",
                "foulen",
                "fouleni",
                "male",
                new Date(System.currentTimeMillis()),
                "12345678",
                new Timestamp(System.currentTimeMillis()),
                false,
                null,
                false,
                null
        );

        try {
            us.ajouter(user);
            user.setEmail("foulen@gmail.com");
            us.modifier(user);
            System.out.println(us.afficher());
            System.out.println(us.afficher(user));
            us.supprimer(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
