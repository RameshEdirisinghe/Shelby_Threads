package service.custom.impl;


import Entity.UserEntity;
import com.google.inject.Inject;
import model.User;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.UserDao;
import service.custom.UserService;
import util.DaoType;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class UserServiceImpl implements UserService {
    ModelMapper mapper = new ModelMapper();
    @Inject
    UserDao userDao;

    @Override
    public User LoginAuthenticate(String email, String password) throws SQLException {
        UserEntity userEntity = userDao.LoginAuthenticate(email, password);
        return userEntity != null ? mapper.map(userEntity, User.class) : null;
    }

    @Override
    public boolean isExist(String email) throws SQLException {
        return userDao.isExist(email);
    }

    @Override
    public String sendOtp(String email) throws MessagingException {
        //genarate random otp
        String otp = "";
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            otp += rand.nextInt(10);
        }

        //send Email
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.user", "mkcomputeredu03@gmail.com");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.debug", true);
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", false);

        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        String emailBody = "<html>" +
                "<body style='font-family:Arial,sans-serif; line-height:1.6;'>" +
                "<h2 style='color:#2E86C1;'>Shelby Threads Account OTP Verification</h2>" +
                "<p>Dear user,</p>" +
                "<p>Your one-time password (OTP) for accessing your Shelby Threads account is:  </p>" +
                "<h1 style='background-color:#F4F4F4; padding:10px; border-radius:5px; display:inline-block;'>" + otp + "</h1>" +
                "<p>Please use this OTP to complete your login or transaction process.</p>" +
                "<p>If you did not request this, please ignore this email.</p>" +
                "<br><p>Thank you,<br>Shelby Threads Support Team</p>" +
                "</body>" +
                "</html>";

        message.setContent(emailBody, "text/html");

        message.setSubject("OTP For your SHELBY THREADS Account");
        message.setFrom(new InternetAddress("softhiive@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.trim()));
        message.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", "softhiive@gmail.com", "fwow uhmt rwpr kznj");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

        return otp;
    }

    @Override
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        return userDao.updatePassword(email,newPassword);
    }
}
