package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pos.system.project.entity.Item;
import pos.system.project.service.ShortCutService;
import pos.system.project.service.impl.ShortCutServiceImpl;

import java.io.IOException;

import static pos.system.project.controller.OrderController.itemList;
import static pos.system.project.controller.OrderController.shortCutID;

/**
 * @author Amil Srinath
 */
public class ShortcutController {
    ShortCutService shortCutService = new ShortCutServiceImpl();

    @FXML
    public TextField itemBarcode;
    @FXML
    public Label lblItemName;

    @FXML
    public void itemBarcodeOnAction(ActionEvent actionEvent) {
        for (Item item : itemList) {
            if (item.getItemBarcode().equals(itemBarcode.getText())) {
                lblItemName.setText(item.getItemName());
            }
        }
    }

    @FXML
    public void doneOnAction(ActionEvent actionEvent) throws IOException {
        shortCutService.saveShortCut(shortCutID, itemBarcode.getText());
        Stage stage = (Stage) itemBarcode.getScene().getWindow();
        stage.close();
    }
}
