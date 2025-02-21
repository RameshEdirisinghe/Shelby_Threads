package repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T,ID> extends SuperDao {
    boolean save(T entity) throws SQLException;
    boolean update(ID id,T entity);
    boolean delete(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
    T search(ID id) throws SQLException;
}
