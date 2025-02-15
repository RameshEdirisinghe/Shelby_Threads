package controller.login;

import controller.home.HomeFormController;
import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;


public class LoginFormController {

  
    public AnchorPane img1AnchorPane;
    public AnchorPane img2AnchorPane;
    public AnchorPane img3AnchorPane;
    public PasswordField txtPassword;
    public AnchorPane recoveryPasswordAnchorPane;
    public AnchorPane loginAnchorPane;
    public TextField txtemail;
    public TextField txtRandomOtp;
    public PasswordField txtNewPassword;
    public TextField txtResetEmail;
    public TextField txtOtp;
    public PasswordField txtConfirmPassword;
    public String otp;


    public void mounseOnClickimg3(MouseEvent mouseEvent) {

        img1AnchorPane.setVisible(false);     // Hide img1AnchorPane
        img2AnchorPane.setVisible(false);     // Hide img2AnchorPane
        img3AnchorPane.setVisible(true);      // Show img3AnchorPane
        new FadeIn(img3AnchorPane).play();    // Apply FadeIn animation
        img3AnchorPane.toFront();
    }

    public void mounseOnClickimg2(MouseEvent mouseEvent) {

        img1AnchorPane.setVisible(false);     // Hide img1AnchorPane
        img2AnchorPane.setVisible(true);      // Show img2AnchorPane
        img3AnchorPane.setVisible(false);     // Hide img3AnchorPane
        new FadeIn(img2AnchorPane).play();    // Apply FadeIn animation
        img2AnchorPane.toFront();
    }

    public void mounseOnClickimg1(MouseEvent mouseEvent) {

        img1AnchorPane.setVisible(true);      // Show img1AnchorPane
        img2AnchorPane.setVisible(false);     // Hide img2AnchorPane
        img3AnchorPane.setVisible(false);     // Hide img3AnchorPane
        new FadeIn(img1AnchorPane).play();    // Apply FadeIn animation
        img1AnchorPane.toFront();
    }

    public void btnOnActionRecoveryPassword(ActionEvent actionEvent) {
        loginAnchorPane.setVisible(false);
        new FadeIn(recoveryPasswordAnchorPane).play();
        recoveryPasswordAnchorPane.toFront();
        if (txtemail.getText()!=null) {
            txtResetEmail.setText(txtemail.getText());
        }
    }

    public void btnOnClickActionLogin(ActionEvent actionEvent) {
        String usrId = LoginController.getInstance().login(txtemail.getText(),txtPassword.getText());
        if(usrId!=null){
            System.out.println("awa");
            txtemail.clear();
            txtPassword.clear();
            newForm(usrId);
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Invalid user Name or Password").show();
        }
    }

    public void newForm(String usrId){
        Stage st = new Stage();
        try {
            HomeFormController.setUserId(usrId);
            st.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Home.fxml"))));
            st.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mouseOnActionLogin(MouseEvent mouseEvent) {
//        recoveryPasswordAnchorPane.setVisible(false);
        loginAnchorPane.setVisible(true);
        new FadeIn(loginAnchorPane).play();
        loginAnchorPane.toFront();
    }

    public void random(){
        otp = "";

        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            otp += rand.nextInt(10);
        }

        txtRandomOtp.setText(""+otp);
    }

    public void sendOtp(){
        random();
        Properties props=new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port",465);
        props.put("mail.smtp.user","mkcomputeredu03@gmail.com");
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.enable",true);
        props.put("mail.smtp.debug",true);
        props.put("mail.smtp.socketFactory.port",465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback",false);

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            String emailBody = "<html>" +
                    "<body style='font-family:Arial,sans-serif; line-height:1.6;'>" +
                    "<h2 style='color:#2E86C1;'>Shelby Threads Account OTP Verification</h2>" +
                    "<p>Dear user,</p>" +
                    "<p>Your one-time password (OTP) for accessing your Shelby Threads account is:  </p>" +
                    "<h1 style='background-color:#F4F4F4; padding:10px; border-radius:5px; display:inline-block;'>" +  txtRandomOtp.getText() + "</h1>" +
                    "<p>Please use this OTP to complete your login or transaction process.</p>" +
                    "<p>If you did not request this, please ignore this email.</p>" +
                    "<br><p>Thank you,<br>Shelby Threads Support Team</p>" +
                    "</body>" +
                    "</html>";

            message.setContent(emailBody, "text/html");

            message.setSubject("OTP For your SHELBY THREADS Account");
            message.setFrom(new InternetAddress("softhiive@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(txtResetEmail.getText().trim()));
            message.saveChanges();
            try
            {
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com","softhiive@gmail.com","fwow uhmt rwpr kznj");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();

                txtResetEmail.setEditable(false);
                txtOtp.setEditable(true);
                txtNewPassword.setEditable(true);
                txtConfirmPassword.setEditable(true);
                JOptionPane.showMessageDialog(null,"OTP has send to your Email id");
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(null,"Please check your internet connection");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e);
        }
    }

    public void btnOnClickActionSendOtp(ActionEvent actionEvent) {
        if (LoginController.getInstance().isExist(txtResetEmail.getText())){
            sendOtp();
        }else{
            JOptionPane.showMessageDialog(null,"This Email Address not Registered");
        }

    }

    public void btnOnClickActionSubmit(ActionEvent actionEvent) {
        loginAnchorPane.setVisible(true);
        new FadeIn(loginAnchorPane).play();
        loginAnchorPane.toFront();

        if (otp.equals(txtOtp.getText())) {
            LoginController.getInstance().updatePassword(txtResetEmail.getText(), txtNewPassword.getText());
            JOptionPane.showMessageDialog(null,"Password Changed SuccessFully!");
        }else{
            JOptionPane.showMessageDialog(null,"Incorrect OTP!");
        }

    }
}
