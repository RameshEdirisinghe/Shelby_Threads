package service.custom;

import model.Product;
import service.SuperService;

import java.util.List;

public interface ProductService extends SuperService {

    boolean addProducts(Product product);
    List<Product> getAll();
    boolean deleteProduct(Integer productId);
    Product searchProduct(Integer productId);
    boolean updateProduct(Product product);
    Product getProduct(String productName);

}
