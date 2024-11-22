package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos.system.project.service.UserService;
import pos.system.project.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class LoginController {
    UserService userService = new UserServiceImpl();

    @FXML
    public TextField cusUsername;
    @FXML
    public TextField cusPassword;
    @FXML
    public AnchorPane root;

    @FXML
    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        cusUsername.setText("amilsrinath");
        cusPassword.setText("1234");

        if (userService.checkCredentials(cusUsername.getText(), cusPassword.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();

            HomeController controller = loader.getController();
            controller.setUserID(cusUsername.getText());

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            root.getScene().getWindow().hide();
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid Username or Password").show();
        }
    }

    @FXML
    public void lblFogotPasswordOnMouseClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fogotPassword.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            root.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
