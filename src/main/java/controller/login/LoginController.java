package controller.login;

import DBConnection.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public static LoginController instance;

    private  LoginController(){

    }

    public static LoginController getInstance(){
        return instance==null?instance=new LoginController():instance;
    }

    public String login(String email,String Password) {
        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select * from User");

            while (rst.next()) {
                if (rst.getString(3).equals(email)) {
                    ResultSet rst1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select * from User where Email='" + email + "'");
                    if (rst.getString(4).equals(Password)) {
                        return rst.getString(1);
                    }
                }

            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExist(String Email) {

        try {
            ResultSet rst = DBConnection.getInstance().getConnection().createStatement().executeQuery("Select Email from user where Email='"+Email+"'");

            if (rst.next()){
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean updatePassword(String Email,String newPassword){

        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement("UPDATE user SET password = '"+newPassword+"' WHERE Email ='"+Email+"'");
            return stm.executeUpdate()>0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
