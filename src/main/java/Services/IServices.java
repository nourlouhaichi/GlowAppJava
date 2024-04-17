package Services;

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

    public List<T> afficher() throws SQLException;

    T afficher(T t) throws  SQLException;

    Publication afficher(int id) throws SQLException;
}
