package service.custom.impl;

import com.google.inject.Inject;
import model.Supplier;
import repository.custom.SupplierDao;
import service.custom.SupplierService;

import java.sql.SQLException;
import java.util.List;

public class SupplierServiceImpl implements SupplierService {

    @Inject
    SupplierDao supplierDao;

    @Override
    public int getAllSupplierCount() {
        try {
            return supplierDao.getAllSupplierCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllSupplierNames() {
        try {
            return supplierDao.getAllSupplierNames();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Supplier> getAll() {
        try {
            return supplierDao.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
