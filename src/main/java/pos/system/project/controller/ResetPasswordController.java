package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos.system.project.service.UserService;
import pos.system.project.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class ResetPasswordController {

    UserService userService = new UserServiceImpl();
    String email;

    @FXML
    public PasswordField txtRePassword;

    @FXML
    public PasswordField txtPassword;

    @FXML
    public void btnDoneOnAction(ActionEvent actionEvent) throws IOException {
        if (txtPassword.getText().equals(txtRePassword.getText())) {
            userService.resetPassword(email, txtPassword.getText());
            txtPassword.getScene().getWindow().hide();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }else {
            new Alert(Alert.AlertType.ERROR, "Password not matched").show();
        }
    }

    public void setUserEmail(String text) {
        this.email = text;
    }
}
