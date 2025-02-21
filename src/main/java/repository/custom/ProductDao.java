package repository.custom;

import Entity.ProductEntity;
import model.Order;
import model.Product;
import repository.CrudDao;

public interface ProductDao extends CrudDao<ProductEntity,Integer> {

    boolean updateStock(Order entity);
}
