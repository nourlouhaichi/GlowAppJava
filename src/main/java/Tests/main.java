package Tests;

import java.sql.Date;
import java.sql.Timestamp;

import Services.GMailer;
import Services.UserService;
import java.sql.SQLException;

import Utils.MyDatabase;
import Entities.User;

public class main extends Object {

    public static void main(String[] args) {

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

        MyDatabase bd = MyDatabase.getInstance();
        UserService us = new UserService();

        try {
            GMailer mail = new GMailer();
            /*mail.sendMail("A new message", """
                    Dear reader,
                                    
                    Hello world.
                                    
                    Best regards,
                    myself
                    """);*/
            //us.ajouter(user);
            user.setEmail("foulen@gmail.com");
            //us.modifier(user);
            System.out.println(us.afficher());
            //us.supprimer(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
