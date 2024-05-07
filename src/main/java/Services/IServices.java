package Services;

import Entities.Programme;

import java.sql.SQLException;
import java.util.List;
public interface IServices<T> {

    void add(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(T t) throws SQLException;

    public List<T> show() throws SQLException;

    T afficher(T t) throws  SQLException;

    public void ajouter(T t) throws SQLException;


    public void modifier(T t) throws SQLException;


    public void supprimer(T t) throws SQLException;

    public void supprimer(int id)throws SQLException;

    public List<T> afficher() throws SQLException;
  

}
