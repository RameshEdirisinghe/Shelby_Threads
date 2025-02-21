package service.custom.impl;

import com.google.inject.Inject;
import model.Product;
import repository.custom.ProductDao;
import service.custom.ProductService;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Inject
    ProductDao productDao;

    @Override
    public boolean addProducts(Product product) {
        return false;
    }

    @Override
    public List<Product> getAll() {
        try {
            return productDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
