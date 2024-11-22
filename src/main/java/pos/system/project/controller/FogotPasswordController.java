package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pos.system.project.service.UserService;
import pos.system.project.service.impl.UserServiceImpl;
import pos.system.project.util.JavaMailUtil;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class FogotPasswordController {

    UserService userService = new UserServiceImpl();
    private int otp;

    @FXML
    public Button btnBack;

    @FXML
    public AnchorPane root;

    @FXML
    public Text lblError;

    @FXML
    public TextField txtOTP;

    @FXML
    public TextField txtEmail;

    @FXML
    public void initialize() {
        lblError.setVisible(false);
    }

    @FXML
    public void btnSendOTPOnAction(ActionEvent actionEvent) throws IOException, MessagingException {
        boolean isUserExist = userService.isUserExist(txtEmail.getText());

        if (isUserExist) {
            lblError.setVisible(false);
            otp = generateOTP();
            JavaMailUtil.sendMail(txtEmail.getText(), otp);
        } else {
            lblError.setVisible(true);
        }
    }

    @FXML
    public void txtOTPOnKeyRelesed(KeyEvent keyEvent) {
        String otpText = txtOTP.getText();
        int length = otpText.length();

        if (length == 4) {
            if (otpText.equals(String.valueOf(otp))) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/resetPassword.fxml"));
                    AnchorPane anchorPane = loader.load();

                    ResetPasswordController controller = loader.getController();
                    controller.setUserEmail(txtEmail.getText());

                    Scene scene = new Scene(anchorPane);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    root.getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                lblError.setVisible(true);
            }
        }
    }

    public int generateOTP() {
        int min = 1000;
        int max = 9999;
        return (int) ((Math.random() * (max - min)) + min);
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {
        btnBack.getScene().getWindow().hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
