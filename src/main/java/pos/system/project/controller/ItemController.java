package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class ItemController {
    @FXML
    public Button btnBack;
    @FXML
    public TextField itemSellingPrice;
    @FXML
    public TextField itemPurchasePrice;
    @FXML
    public TextField itemQty;
    @FXML
    public TextField itemName;
    @FXML
    public Text lblPurchasePrice;
    @FXML
    public Text lblQuantity;
    @FXML
    public Text lblSellingPrice;
    @FXML
    public ComboBox<String> cmbSellBy;

    @FXML
    public void initialize() {
        cmbSellBy.getItems().addAll("Unit", "Fraction");
        cmbSellBy.getSelectionModel().select(0);

        cmbSellBy.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Unit")) {
                lblQuantity.setText("Quantity");
                lblPurchasePrice.setText("Purchase Price(unit)");
                lblSellingPrice.setText("Selling Price(unit)");
            } else if (newValue.equals("Fraction")) {
                lblQuantity.setText("Quantity");
                lblPurchasePrice.setText("Purchase Price(1Kg)");
                lblSellingPrice.setText("Selling Price(1Kg)");
            }
        });
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {
        btnBack.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
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

    @FXML
    public void AddOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void EditOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void DeleteOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void tableOnMouseClicked(MouseEvent mouseEvent) {

    }

    @FXML
    public void ClearOnAction(ActionEvent actionEvent) {

    }
}
