package repository.custom;

import model.Supplier;
import repository.CrudDao;

import java.sql.SQLException;
import java.util.List;

public interface SupplierDao extends CrudDao<Supplier,Integer> {
    int getAllSupplierCount() throws SQLException;
    List<String> getAllSupplierNames() throws SQLException;
}
