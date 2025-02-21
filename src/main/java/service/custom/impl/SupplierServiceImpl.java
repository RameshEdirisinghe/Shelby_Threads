package service.custom.impl;

import Entity.ProductEntity;
import Entity.SupplierEntity;
import com.google.inject.Inject;
import model.Product;
import model.Supplier;
import org.modelmapper.ModelMapper;
import repository.custom.SupplierDao;
import service.custom.SupplierService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierServiceImpl implements SupplierService {

    @Inject
    SupplierDao supplierDao;

    ModelMapper modelMapper = new ModelMapper();

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
            List<SupplierEntity> supplierEntities = supplierDao.getAll();
            List<Supplier> suppliers = new ArrayList<>();

            supplierEntities.forEach(supplier -> {
                suppliers.add(modelMapper.map(supplier,Supplier.class));
            });
            return suppliers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        return supplierDao.update(supplier.getId(),modelMapper.map(supplier, SupplierEntity.class));
    }

    @Override
    public Supplier searchSupplier(Integer supplierId) {
        SupplierEntity supplierEntity = null;
        try {
            supplierEntity = supplierDao.search(supplierId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return supplierEntity!=null?modelMapper.map(supplierEntity,Supplier.class):null;
    }

    @Override
    public boolean deleteSupplier(Integer supplierId) {
        try {
            return supplierDao.delete(supplierId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addSupplier(Supplier supplier) {
        try {
            return supplierDao.save(modelMapper.map(supplier,SupplierEntity.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
