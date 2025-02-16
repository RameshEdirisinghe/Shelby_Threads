package service.custom.impl;

import DBConnection.DBConnection;
import service.custom.CustomerService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerServiceImpl implements CustomerService {
    @Override
    public int getCustomersCount() {


        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) AS customer_count FROM customer");
            while (rst.next()) {
                return (rst.getInt(1));
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
