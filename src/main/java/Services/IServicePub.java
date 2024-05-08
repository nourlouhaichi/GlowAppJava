package Services;

import Entities.Comment;
import Entities.Publication;
import Entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IServicePub<T> {
    void ajouter(Publication Publication) throws SQLException;

    void modifier(Publication Publication) throws SQLException;

    public void ajouter(T t) throws SQLException;

    public void modifier(T t) throws SQLException;

    void ajouter(Comment comment) throws SQLException;

    void modifier(Comment comment) throws SQLException;

    void supprimer(User user) throws SQLException;

    public void supprimer(int id) throws SQLException;

    void supprimer(Publication publication) throws SQLException;

    void supprimer(Comment comment) throws SQLException;

    public List<T> afficher() throws SQLException;

    public List<T> affiche(int publication_id) throws SQLException;

    Comment afficher(Comment comment) throws SQLException;

    T afficher(T t) throws  SQLException;

    Publication afficher(Publication publication) throws SQLException;

    List<Comment> afficher(int id) throws SQLException;

    void ajouterWithImage(Publication publication, byte[] imageData) throws SQLException;
}
