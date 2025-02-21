package controller.user;

import DBConnection.DBConnection;
import model.Employee;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserController {
    private static UserController instance;

    private UserController() {
    }

    public static UserController getInstance() {
        return instance == null ? instance = new UserController() : instance;
    }

    public boolean addUser(Employee employee){

        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("Insert Into user(Name,Email,Password,Role) values (?,?,?,?)");
            stm.setString(1,employee.getName());
            stm.setString(2,employee.getEmail());
            stm.setString(3,employee.getPassword());
            stm.setString(4, "controller");

            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public boolean updateUser(Employee employee){
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE user SET Name = ?, Email = ?, password = ? WHERE Name =?;");
            stm.setString(1,employee.getName());
            stm.setString(2,employee.getEmail());
            stm.setString(3,employee.getPassword());
            stm.setString(4,employee.getName());
            System.out.println("hi3");

            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteUser(String userName){
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM user WHERE UserID=?");
            stm.setString(1, userName);

            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
