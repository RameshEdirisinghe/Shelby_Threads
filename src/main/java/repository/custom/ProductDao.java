package repository.custom;

import Entity.ProductEntity;
import model.Order;
import model.Product;
import repository.CrudDao;

import java.sql.SQLException;

public interface ProductDao extends CrudDao<ProductEntity,Integer> {

    boolean updateStock(Order entity);

    ProductEntity searchByName(String productName) throws SQLException;
}
