package Services;

import Entities.Comment;
import Entities.Publication;
import Entities.User;

import java.sql.SQLException;
import java.util.List;
public interface IServices <T>{
    public void ajouter(T t) throws SQLException;

    public void modifier(T t) throws SQLException;

    void supprimer(User user) throws SQLException;

    public void supprimer(int id) throws SQLException;

    void supprimer(Publication publication) throws SQLException;

    void supprimer(Comment comment) throws SQLException;

    public List<T> afficher() throws SQLException;

    public List<T> affiche(int publication_id) throws SQLException;

    T afficher(T t) throws  SQLException;

    List<Comment> afficher(int id) throws SQLException;
}
