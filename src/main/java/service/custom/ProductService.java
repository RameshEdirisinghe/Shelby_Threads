package service.custom;

import model.Product;
import service.SuperService;

public interface ProductService extends SuperService {

    boolean addProducts(Product product);
}
