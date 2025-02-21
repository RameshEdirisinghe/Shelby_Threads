package service.custom;

import model.Product;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {

    boolean addProducts(Product product);

    List<Product> getAll();
}
