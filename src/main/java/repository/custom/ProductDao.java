package repository.custom;

import model.Order;
import model.Product;
import repository.CrudDao;

public interface ProductDao extends CrudDao<Product,Integer> {

    boolean updateStock(Order entity);
}
