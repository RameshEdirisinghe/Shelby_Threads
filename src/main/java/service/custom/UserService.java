package service.custom;

import model.User;
import service.SuperService;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface UserService extends SuperService {
    User LoginAuthenticate(String email, String password) throws SQLException;

    boolean isExist(String email) throws SQLException;

    String sendOtp(String email) throws MessagingException;

    boolean updatePassword(String email,String newPassword) throws SQLException;
}
