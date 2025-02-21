package service.custom;

import model.Supplier;

import java.util.List;

public interface SupplierService {
    int getAllSupplierCount();
    List<String> getAllSupplierNames();
    List<Supplier> getAll();
}
