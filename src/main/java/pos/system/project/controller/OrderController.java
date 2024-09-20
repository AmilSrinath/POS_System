package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Item;
import pos.system.project.service.BadgeService;
import pos.system.project.service.ItemService;
import pos.system.project.service.impl.BadgeServiceImpl;
import pos.system.project.service.impl.ItemServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class OrderController {
    ItemService itemService = new ItemServiceImpl();
    BadgeService badgeService = new BadgeServiceImpl();

    public static List<Item> itemList;
    public static List<Badge> badgeList;


    @FXML
    public TextField itemBarcode;

    @FXML
    public Button btnBack;

    public void initialize() throws IOException {
        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();
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

    public void PlaceOrderOnAction(ActionEvent actionEvent) {

    }

    @FXML
    public void itemBarcodeOnAction(ActionEvent actionEvent) {
        boolean itemFound = false; // Track if an item is found
        for (Item item : itemList) {
            if (item.getItemBarcode().equals(itemBarcode.getText())) {
                itemFound = true; // Set the flag if an item is found
                List<Badge> itemBadges = badgeList.stream()
                        .filter(badge -> badge.getItem().getItemId() == item.getItemId())
                        .toList();

                if (itemBadges.size() == 1) {
                    // If there's only one badge, directly show quantity input without popup
                    Badge singleBadge = itemBadges.get(0);
                    if (singleBadge.getStatus() == 1) {
                        PopupController popupController = new PopupController();
                        popupController.setOrderController(this, item.getItemId());
                        popupController.showQuantityInputDialog(singleBadge, null); // Null popupStage since we don't need one
                    } else {
                        showErrorDialog("Inactive Badge", "This badge is not active.");
                    }
                } else if (itemBadges.size() > 1) {
                    // Multiple badges, show the popup
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/popup.fxml"));
                        AnchorPane popupRoot = loader.load();

                        PopupController popupController = loader.getController();
                        popupController.setOrderController(this, item.getItemId());

                        Stage popupStage = new Stage();
                        popupStage.setScene(new Scene(popupRoot));
                        popupStage.initModality(Modality.APPLICATION_MODAL);

                        popupController.loadBadges(itemBadges, popupStage);

                        popupStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        showErrorDialog("Error", "Failed to load the popup. Please try again.");
                    }
                } else {
                    showErrorDialog("No Badges Found", "This item has no active badges.");
                }
            }
        }

        if (!itemFound) {
            showErrorDialog("Invalid Barcode", "No item found for the entered barcode.");
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
