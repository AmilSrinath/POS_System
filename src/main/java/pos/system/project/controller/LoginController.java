package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class LoginController {
    @FXML
    public TextField cusUsername;
    @FXML
    public TextField cusPassword;
    @FXML
    public AnchorPane root;

    @FXML
    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
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
    }
}
