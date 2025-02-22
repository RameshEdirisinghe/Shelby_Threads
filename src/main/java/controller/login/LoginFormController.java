package controller.login;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
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
import model.User;
import service.custom.UserService;
import util.AppModule;

import javax.mail.MessagingException;
import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;


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

    @Inject
    UserService userService;

    //login page slideShow Animation
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


    public void btnOnClickActionLogin(ActionEvent actionEvent) {
        try {
            User user = userService.LoginAuthenticate(txtemail.getText(), txtPassword.getText());

            if (user != null) {
                txtemail.clear();
                txtPassword.clear();
                newForm(user);
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Invalid user Name or Password").show();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void newForm(User user) {
        Stage st = new Stage();
        try {
            if (user.getRole().equals("Admin")) {
                Injector injector = Guice.createInjector(new AppModule());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboardForm.fxml"));
                loader.setControllerFactory(injector::getInstance);
                st.setScene(new Scene(loader.load()));

            }else{
                Injector injector = Guice.createInjector(new AppModule());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EmployeeDashboard.fxml"));
                loader.setControllerFactory(injector::getInstance);
                st.setScene(new Scene(loader.load()));

            }
            st.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnOnActionRecoveryPassword(ActionEvent actionEvent) {
        loginAnchorPane.setVisible(false);
        new FadeIn(recoveryPasswordAnchorPane).play();
        recoveryPasswordAnchorPane.toFront();
        if (txtemail.getText() != null) {
            txtResetEmail.setText(txtemail.getText());
        }
    }

    //backto Login page from Recovery page
    public void mouseOnActionLogin(MouseEvent mouseEvent) {
        loginAnchorPane.setVisible(true);
        new FadeIn(loginAnchorPane).play();
        loginAnchorPane.toFront();
    }

    public void btnOnClickActionSendOtp(ActionEvent actionEvent) {
        try {
            if (userService.isExist(txtResetEmail.getText())) {

               this.otp = userService.sendOtp(txtResetEmail.getText());

                txtResetEmail.setEditable(false);
                txtOtp.setEditable(true);
                txtNewPassword.setEditable(true);
                txtConfirmPassword.setEditable(true);
                JOptionPane.showMessageDialog(null, "OTP has send to your Email id");

            } else {
                JOptionPane.showMessageDialog(null, "This Email Address not Registered");
            }

        } catch (SQLException | MessagingException e) {
            JOptionPane.showMessageDialog(null, "Please check your internet connection & Try again later");
            throw new RuntimeException(e);
        }

    }

    public void btnOnClickActionSubmit(ActionEvent actionEvent) {

        if (otp.equals(txtOtp.getText())) {
            try {
                userService.updatePassword(txtResetEmail.getText(), txtNewPassword.getText());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(null, "Password Changed SuccessFully!");
            loginAnchorPane.setVisible(true);
            new FadeIn(loginAnchorPane).play();
            loginAnchorPane.toFront();
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect OTP!");
        }

    }
}
