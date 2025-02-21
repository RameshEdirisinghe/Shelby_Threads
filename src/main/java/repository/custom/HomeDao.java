package repository.custom;

import model.ProductSales;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface HomeDao {

    List<ProductSales> getTop3Product() throws SQLException;

    Map<Integer, Map<String, Integer>> getSalesData(List<String> topProducts) throws SQLException;
}
