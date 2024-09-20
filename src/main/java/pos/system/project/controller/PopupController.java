package pos.system.project.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pos.system.project.entity.Badge;

import java.util.List;
import java.util.Optional;

/**
 * @author Amil Srinath
 */
public class PopupController {

    public VBox badgeContainer;
    public int itemId = -1;

    private OrderController orderController; // Reference to the OrderController

    public void setOrderController(OrderController orderController, int itemId) {
        this.orderController = orderController;
        this.itemId = itemId;
    }

    public void loadBadges(List<Badge> badgeList, Stage popupStage) {
        // Filter the badge list to get only active badges for the selected item
        List<Badge> filteredBadges = badgeList.stream()
                .filter(badge -> badge.getStatus() == 1 && badge.getItem().getItemId() == itemId)
                .toList();

        // If there is only one badge, automatically select it and show quantity input
        if (filteredBadges.size() == 1) {
            Badge badge = filteredBadges.get(0);
            // Set the badge ID to itemBarcode in the OrderController
            orderController.itemBarcode.setText(String.valueOf(badge.getBadgeId()));

            // Automatically prompt the user for quantity input
            showQuantityInputDialog(badge, popupStage);

            // Close the popup stage after handling the input
            popupStage.close();
        } else {
            // If multiple badges, load them into the popup and display them
            setTableData(filteredBadges, popupStage, orderController);
            popupStage.show(); // Show the popup with badges
        }
    }

    public void showQuantityInputDialog(Badge badge, Stage popupStage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Quantity");
        dialog.setHeaderText("Enter the quantity for Badge ID: " + badge.getBadgeId());
        dialog.setContentText("Quantity:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(quantity -> {
            try {
                int qty = Integer.parseInt(quantity);
                if (qty > badge.getQuantity()) {
                    showErrorDialog("Invalid Quantity", "Entered quantity exceeds available stock.");
                } else if (qty <= 0) {
                    showErrorDialog("Invalid Quantity", "Quantity must be greater than zero.");
                } else {
                    // Handle the case where the quantity is valid
                    // You could store this quantity in the OrderController or proceed with the order processing
                    orderController.itemBarcode.setText(String.valueOf(badge.getBadgeId()));

                    // Close the popup stage if it's open
                    if (popupStage != null) {
                        popupStage.close();
                    }
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter a valid number for quantity.");
            }
        });
    }

    public void setTableData(List<Badge> badges, Stage popupStage, OrderController orderController) {
        badgeContainer.getChildren().clear();
        for (Badge badge : badges) {
            if (badge.getStatus() == 1 && badge.getItem().getItemId() == itemId) {
                AnchorPane badgeCard = createBadgeCard(badge, popupStage, orderController);
                badgeContainer.getChildren().add(badgeCard);
            }
        }
    }

    private AnchorPane createBadgeCard(Badge badge, Stage popupStage, OrderController orderController) {
        // Create an AnchorPane to represent the card
        AnchorPane card = new AnchorPane();
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");
        card.setPrefSize(600, 100);

        // Badge ID Label
        Label badgeIdLabel = new Label("Badge ID: " + badge.getBadgeId());
        badgeIdLabel.setLayoutX(20);
        badgeIdLabel.setLayoutY(20);

        // Status Label
        Label badgeStatusLabel = new Label("Status: " + (badge.getStatus() == 1 ? "Active" : "Inactive"));
        badgeStatusLabel.setLayoutX(20);
        badgeStatusLabel.setLayoutY(50);

        // Quantity Label
        Label quantityLabel = new Label("Quantity: " + badge.getQuantity());
        quantityLabel.setLayoutX(20);
        quantityLabel.setLayoutY(80);

        // Selling Price Label
        Label priceLabel = new Label("Selling Price: " + badge.getSellingPrice());
        priceLabel.setLayoutX(300);
        priceLabel.setLayoutY(20);

        // Expiration Date Label
        Label expiryDateLabel = new Label("Expiry Date: " + badge.getExpireDate().toString());
        expiryDateLabel.setLayoutX(300);
        expiryDateLabel.setLayoutY(50);

        // Add hover effect to the card
        card.setOnMouseEntered(event -> {
            card.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-color: #aaa;");
        });
        card.setOnMouseExited(event -> {
            card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");
        });

        // Add click event to the card to hide window and set the badge ID for itemBarcode
        card.setOnMouseClicked(event -> {
            // Set the badge ID to itemBarcode in the OrderController
            orderController.itemBarcode.setText(String.valueOf(badge.getBadgeId()));

            // Show a popup to enter quantity (same logic as in loadBadges)
            showQuantityInputDialog(badge, popupStage);
        });

        // Add components to the card
        card.getChildren().addAll(badgeIdLabel, badgeStatusLabel, quantityLabel, priceLabel, expiryDateLabel);

        return card;
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
