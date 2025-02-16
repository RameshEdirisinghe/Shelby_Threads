package controller.employee;

import DBConnection.DBConnection;
import controller.user.UserController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeController {
    private static EmployeeController instance;
    public TextField txtEmpPassword;
    public TextField txtEmpEmail;
    public TextField txtEmpName;
    public TextField txtSearchEmpId;
    public TableColumn colEmpId;
    public TableView tblEmployee;
    public TableColumn colEmpName;
    public TableColumn colEmpEmail;
    public TableColumn colEmpCompany;
    public TextField txtEmpCompany;

    private EmployeeController() {
    }

    public static EmployeeController getInstance() {
        return instance == null ? instance = new EmployeeController() : instance;
    }


    public List<Employee> getAllEmployee() {
        List<Employee> employeeList = new ArrayList<>();

        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM Employee");
            while (rst.next()) {

                    employeeList.add(new Employee(rst.getInt(1), rst.getString(2), rst.getString(3), "null", rst.getString(4)));


            }
            return employeeList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public Employee searchEmployee(String empId) {
        try {
            String query = "SELECT * FROM employee WHERE EmployeeID=?";
            PreparedStatement stmt = DBConnection.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, empId); // Set empId parameter
            ResultSet rst = stmt.executeQuery();

            if (rst.next()) {



                    return new Employee(rst.getInt(1), rst.getString(2), rst.getString(3), "null", rst.getString(4));

            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getEmpRoles(){
        List<String> roleList = new ArrayList<>();
        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select role_name from role");
            while (rst.next()) {
                roleList.add(rst.getString(1));
            }
            return roleList;
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public boolean addEmployee(Employee employee){
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("Insert Into Employee(Name,Email,company) values (?,?,?)");
            stm.setString(1,employee.getName());
            stm.setString(2,employee.getEmail());
            stm.setString(3,employee.getCompany());

            if (stm.executeUpdate()>0){
                boolean isAdd = UserController.getInstance().addUser(employee);
                if (isAdd){
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public boolean updateEmployee(Employee employee){
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("UPDATE employee SET Name = ?, Email = ?, company = ? WHERE employeeid =?;");
            stm.setString(1,employee.getName());
            stm.setString(2,employee.getEmail());
            stm.setString(3,employee.getCompany());
            stm.setInt(4,employee.getId());

            if (stm.executeUpdate()>0){
                System.out.println("hi1");
                boolean isUpdate = UserController.getInstance().updateUser(employee);
                if (isUpdate){
                    System.out.println("hi2");
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public boolean deleteEmployee(String empId,String name){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);


            PreparedStatement stm = connection.prepareStatement("DELETE FROM employee WHERE EmployeeID=?");
            stm.setString(1, empId);

            int rowsAffected = stm.executeUpdate();


            if (rowsAffected > 0) {
                boolean isUpdate = UserController.getInstance().deleteUser(name);
                if (isUpdate) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
