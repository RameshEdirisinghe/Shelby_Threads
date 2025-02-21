package service.custom;

import model.Supplier;
import service.SuperService;

import java.util.List;

public interface SupplierService extends SuperService {
    int getAllSupplierCount();
    List<String> getAllSupplierNames();
    List<Supplier> getAll();
    boolean updateSupplier(Supplier supplier);
    Supplier searchSupplier(Integer supplierId);
    boolean deleteSupplier(Integer supplierId);
    boolean addSupplier(Supplier supplier);
}
