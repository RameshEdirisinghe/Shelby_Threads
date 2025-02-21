package service.custom.impl;

import Entity.EmployeeEntity;
import Entity.ProductEntity;
import com.google.inject.Inject;
import model.Employee;
import model.Product;
import org.modelmapper.ModelMapper;
import repository.custom.ProductDao;
import service.custom.ProductService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Inject
    ProductDao productDao;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean addProducts(Product product) {
        try {
            return productDao.save(modelMapper.map(product,ProductEntity.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAll() {
        try {
            List<ProductEntity> productEntities = productDao.getAll();
            List<Product> products = new ArrayList<>();

            productEntities.forEach(product -> {
                products.add(modelMapper.map(product,Product.class));
            });
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteProduct(Integer productId) {
        try {
            return productDao.delete(productId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product searchProduct(Integer productId) {
        ProductEntity productEntity = null;
        try {
            productEntity = productDao.search(productId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productEntity!=null?modelMapper.map(productEntity,Product.class):null;
    }

    @Override
    public boolean updateProduct(Product product) {
        return productDao.update(product.getId(),modelMapper.map(product,ProductEntity.class));
    }
}
