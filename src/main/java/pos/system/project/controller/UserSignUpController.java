package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pos.system.project.dto.UserDTO;
import pos.system.project.service.UserService;
import pos.system.project.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class UserSignUpController {
    UserService userService = new UserServiceImpl();

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;

    @FXML
    private PasswordField userRePassword;

    public void initialize() {
        cmbRole.getItems().add("Admin");
        cmbRole.getItems().add("User");
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws IOException {
        UserDTO userDTO = new UserDTO(userName.getText(), userEmail.getText(), userPassword.getText(), cmbRole.getValue());
        if (userPassword.getText().equals(userRePassword.getText())) {
            int added = userService.Add(userDTO);
            if (added == 0) {
                Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) userEmail.getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("POS System");
                stage.show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Password does not match").show();
        }
    }

}
