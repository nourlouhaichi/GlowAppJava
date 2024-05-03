package Services;

import Entities.Programme;

import java.sql.SQLException;
import java.util.List;
public interface IServices <T>{
    public void ajouter(T t) throws SQLException;


    public void modifier(T t) throws SQLException;


    void supprimer(int id) throws SQLException;

    public List<T> afficher() throws SQLException;


}
