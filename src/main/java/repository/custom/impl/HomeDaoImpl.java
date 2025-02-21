package repository.custom.impl;

import DBConnection.DBConnection;
import model.ProductSales;
import repository.custom.HomeDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class HomeDaoImpl implements HomeDao {
    @Override
    public List<ProductSales>getTop3Product() throws SQLException {
        String query = "SELECT op.ProductName, SUM(op.Quantity) AS TotalSold " +
                "FROM OrderProduct op " +
                "GROUP BY op.ProductName " +
                "ORDER BY TotalSold DESC";
        List<ProductSales> productSalesList = new ArrayList<>();

        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                int totalSold = resultSet.getInt("TotalSold");
                productSalesList.add(new ProductSales(productName, totalSold));
            }

        return productSalesList;


    }

    @Override
    public Map<Integer, Map<String, Integer>> getSalesData(List<String> topProducts) throws SQLException {
        Map<Integer, Map<String, Integer>> salesData = new HashMap<>();
        String query = "SELECT DATE(OrderDate) AS OrderDay, ProductName, SUM(Quantity) AS TotalSold " +
                "FROM orders o JOIN orderProduct op ON o.OrderID = op.OrderID " +
                "WHERE OrderDate >= NOW() - INTERVAL 10 DAY " +
                "AND ProductName IN (?, ?, ?) " +
                "GROUP BY OrderDay, ProductName";

        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(query);

            for (int i = 0; i < topProducts.size(); i++) {
                statement.setString(i + 1, topProducts.get(i));
            }

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int day = rs.getDate("OrderDay").toLocalDate().getDayOfMonth();
                String product = rs.getString("ProductName");
                int sales = rs.getInt("TotalSold");

                salesData.putIfAbsent(day, new HashMap<>());
                salesData.get(day).put(product, sales);
            }


        return salesData;

    }
}
