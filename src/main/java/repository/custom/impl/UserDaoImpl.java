package repository.custom.impl;

import DBConnection.DBConnection;
import Entity.EmployeeEntity;
import Entity.UserEntity;
import repository.custom.UserDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {


    @Override
    public boolean save(UserEntity entity) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Integer integer, UserEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public UserEntity search(Integer integer) {
        return null;
    }

    @Override
    public UserEntity LoginAuthenticate(String email, String password) throws SQLException {

            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select * from User");
            while (rst.next()) {
                if (rst.getString(3).equals(email)) {
                    ResultSet rst1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select * from User where Email='" + email + "'");
                    if (rst.getString(4).equals(password)) {
                       return new UserEntity(rst.getInt(1),null,null,null,rst.getString(5));
                    }
                }

            }
            return null;

    }

    @Override
    public boolean isExist(String email) throws SQLException {
        ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select Email from user where Email='"+email+"'");
        return rst.next()?true:false;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE user SET password = '"+newPassword+"' WHERE Email ='"+email+"'");
        return stm.executeUpdate()>0;
    }

    @Override
    public boolean deleteUser(String userName) throws SQLException {

            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM user WHERE name=?");
            stm.setString(1, userName);

            return stm.executeUpdate()>0;

    }

    @Override
    public boolean addUser(EmployeeEntity employee) throws SQLException {

            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("Insert Into user(Name,Email,Password,Role) values (?,?,?,?)");
            stm.setString(1,employee.getName());
            stm.setString(2,employee.getEmail());
            stm.setString(3,employee.getPassword());
            stm.setString(4, "employee");

            return stm.executeUpdate()>0;

    }

    @Override
    public boolean updateUser(EmployeeEntity employee) throws SQLException {
        PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE user SET Name = ?, Email = ?, password = ? WHERE Name =?;");
        stm.setString(1,employee.getName());
        stm.setString(2,employee.getEmail());
        stm.setString(3,employee.getPassword());
        stm.setString(4,employee.getName());


        return stm.executeUpdate()>0;
    }
}
