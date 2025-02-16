package repository.custom.impl;

import DBConnection.DBConnection;
import model.Order;
import repository.custom.OrderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean save(Order entity) {
        Connection connection = null;
        try {

            String SQL = "INSERT INTO orders(OrderId,TotalCost,PaymentType,EmployeeId) VALUES (?,?,?,?)";

            try {
                connection = DBConnection.getInstance().getConnection();
                connection.setAutoCommit(false);


                PreparedStatement insertStmt = connection.prepareStatement(SQL);
                insertStmt.setInt(1, entity.getOrderId());
                insertStmt.setDouble(2, entity.getTotal());
                insertStmt.setString(3, entity.getPaymenType());
                insertStmt.setInt(4, entity.getEmployeeId());
                boolean isAddedToOrder = insertStmt.executeUpdate() > 0;

                if (isAddedToOrder) {
                    boolean isAddedToOrderDetails = new ProductOrderDaoImpl().save(entity);
                    if (isAddedToOrderDetails) {
                        boolean isUpdatedItem =  new ProductDaoImpl().save(entity);
                        if(isUpdatedItem) {
                            connection.commit();
                            return true;
                        }
                    }
                }
                connection.rollback();
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public boolean update(Integer integer, Order entity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public List<Order> getAll() {
        return List.of();
    }

    @Override
    public String getOrderId() throws SQLException {

        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT OrderID FROM orders ORDER BY OrderID DESC LIMIT 1");
        if (rst.next()){
            return (String.format("%04d",(rst.getInt(1)+1)));
        }

        return "1";
    }

    @Override
    public int getAllOrderCount(){


        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT COUNT(*) AS total_orders FROM orders");
            while (rst.next()) {
                return (rst.getInt(1));
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
