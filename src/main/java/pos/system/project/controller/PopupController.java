package pos.system.project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Item;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pos.system.project.controller.OrderController.badgeList;

/**
 * @author Amil Srinath
 */
public class PopupController {
    @FXML
    public ScrollPane badgeScrollPane;
    @FXML
    public Label lblItemName;
    private int selectedIndex = -1; // To keep track of the currently selected card
    private List<AnchorPane> cardList; // To store the created cards (badges)

    public VBox badgeContainer;
    public int itemId = -1;

    private OrderController orderController; // Reference to the OrderController

    public void setOrderController(OrderController orderController, int itemId) {
        this.orderController = orderController;
        this.itemId = itemId;
    }

    public void loadBadges(List<Badge> badgeList, Stage popupStage) {
        lblItemName.setText(badgeList.get(0).getItem().getItemName());
        cardList = new ArrayList<>();
        badgeContainer.getChildren().clear();

        List<Badge> filteredBadges = badgeList.stream()
                .filter(badge -> badge.getStatus() == 1 && badge.getItem().getItemId() == itemId)
                .sorted((b1, b2) -> b1.getExpireDate().compareTo(b2.getExpireDate()))
                .toList();

        for (Badge badge : filteredBadges) {
            AnchorPane badgeCard = createBadgeCard(badge, popupStage, orderController);
            cardList.add(badgeCard); // Add the card to the list
            badgeContainer.getChildren().add(badgeCard); // Display the card
        }

        // Auto-select the first card if there are any badges
        if (!cardList.isEmpty()) {
            selectedIndex = 0;
            highlightSelectedCard(); // Highlight the first card
        }

        // Add key listener to handle arrow keys and Enter key
        popupStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                navigateCards(1); // Move selection down
            } else if (event.getCode() == KeyCode.UP) {
                navigateCards(-1); // Move selection up
            } else if (event.getCode() == KeyCode.ENTER && selectedIndex >= 0) {
                try {
                    selectBadge(cardList.get(selectedIndex), popupStage); // Select the current card
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        popupStage.show(); // Show the popup with badges
    }

    private void highlightSelectedCard() {
        if (selectedIndex >= 0 && selectedIndex < cardList.size()) {
            if (selectedIndex >= 0) {
                cardList.get(selectedIndex).setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-color: #aaa;");
            }
        }
    }

    private void navigateCards(int direction) {
        // Deselect the current card (if any)
        if (selectedIndex >= 0) {
            cardList.get(selectedIndex).setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");
        }

        // Update the index based on the direction (1 for down, -1 for up)
        selectedIndex += direction;

        // Ensure the index stays within the bounds of the card list
        if (selectedIndex < 0) {
            selectedIndex = cardList.size() - 1; // Wrap around to the last card
        } else if (selectedIndex >= cardList.size()) {
            selectedIndex = 0; // Wrap around to the first card
        }

        // Highlight the new selected card
        cardList.get(selectedIndex).setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-color: #aaa;");

        // Adjust the ScrollPane to ensure the selected card is visible
        scrollToSelectedCard();
    }

    private void scrollToSelectedCard() {
        if (selectedIndex >= 0 && selectedIndex < cardList.size()) {
            AnchorPane selectedCard = cardList.get(selectedIndex);
            double totalHeight = badgeContainer.getHeight()-120; // Total height of the VBox
            double selectedCardY = selectedCard.getBoundsInParent().getMinY(); // Y position of the selected card

            // Calculate the percentage position of the selected card in the ScrollPane
            double scrollPosition = selectedCardY / totalHeight;

            // Ensure the ScrollPane scrolls to show the selected card
            badgeScrollPane.setVvalue(scrollPosition);
        }
    }

    private void selectBadge(AnchorPane selectedCard, Stage popupStage) throws IOException {
        // Assuming the card has a badge associated with it, we can retrieve it based on the UI structure
        Label badgeIdLabel = (Label) selectedCard.getChildren().get(0); // Assuming the first child is the badge ID label
        String badgeIdText = badgeIdLabel.getText().replace("Badge ID: ", "");
        int badgeId = Integer.parseInt(badgeIdText);

        // Find the badge by its ID
        Badge selectedBadge = badgeList.stream()
                .filter(badge -> badge.getBadgeId() == badgeId)
                .findFirst()
                .orElse(null);

        if (selectedBadge != null) {
            showQuantityInputDialog(selectedBadge, popupStage);
            orderController.addRowTable(selectedBadge, selectedBadge.getItem());
        }
    }

    private AnchorPane createBadgeCard(Badge badge, Stage popupStage, OrderController orderController) {
        AnchorPane card = new AnchorPane();
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");
        card.setPrefSize(600, 100);

        Label badgeIdLabel = new Label("Badge ID: " + badge.getBadgeId());
        badgeIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        badgeIdLabel.setLayoutX(20);
        badgeIdLabel.setLayoutY(20);

        Label badgeStatusLabel = new Label("Badge Name: " + badge.getDescription());
        badgeStatusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        badgeStatusLabel.setLayoutX(20);
        badgeStatusLabel.setLayoutY(50);

        Label quantityLabel = new Label("Quantity: " + badge.getQuantity());
        quantityLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        quantityLabel.setLayoutX(20);
        quantityLabel.setLayoutY(80);

        Label priceLabel = new Label("Selling Price: " + badge.getSellingPrice());
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        priceLabel.setLayoutX(300);
        priceLabel.setLayoutY(20);

        Label expiryDateLabel = new Label("Expiry Date: " + badge.getExpireDate().toString());
        expiryDateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        expiryDateLabel.setLayoutX(300);
        expiryDateLabel.setLayoutY(50);

        // Add hover effect to the card
        card.setOnMouseEntered(event -> {
            card.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 10; -fx-border-color: #aaa;");
        });
        card.setOnMouseExited(event -> {
            if (cardList.indexOf(card) != selectedIndex) { // Only change if not the currently selected card
                card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");
            }
        });

        // Add click event to the card to select the badge
        card.setOnMouseClicked(event -> {
            try {
                selectBadge(card, popupStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

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

    public void showQuantityInputDialog(Badge badge, Stage popupStage) {
        TextInputDialog dialog = new TextInputDialog();
        for (Item item : orderController.itemList) {
            if (item.getItemId() == badge.getItem().getItemId()) {
                if (item.getSellByStatus() == 1) {
                    dialog.setTitle("Enter Quantity");
                    dialog.setHeaderText("Enter the quantity for Badge ID: " + badge.getBadgeId() + "\nAvailable Stock: " + badge.getQuantity());
                    dialog.setContentText("Quantity:");
                } else {
                    dialog.setTitle("Enter Quantity");
                    dialog.setHeaderText("Enter the quantity for Badge ID: " + badge.getBadgeId() +
                            "\nAvailable Stock: " + badge.getQuantity() + "g"
                    );
                    dialog.setContentText("Quantity:");
                }
            }
        }

        dialog.getDialogPane().lookup(".text-field").setStyle("-fx-font-size: 18px;");

        // Show dialog and capture result
        Optional<String> result = dialog.showAndWait();

        orderController.isTrue = true;

        // If the dialog was cancelled, do nothing and return
        if (!result.isPresent()) {
            orderController.isTrue = false;
            return;
        }

        result.ifPresent(quantity -> {
            orderController.isTrue = true;
            try {
                BigDecimal qty = new BigDecimal(quantity);

                // Check if the entered quantity exceeds available stock or if it is less than or equal to zero
                if (qty.compareTo(badge.getQuantity()) > 0) {
                    orderController.quantity = BigDecimal.valueOf(-1);
                    showErrorDialog("Invalid Quantity", "Entered quantity exceeds available stock.");
                } else if (qty.compareTo(BigDecimal.ZERO) <= 0) {
                    orderController.quantity = BigDecimal.valueOf(-1);
                    showErrorDialog("Invalid Quantity", "Quantity must be greater than zero.");
                } else {
                    // Valid quantity logic here
                    orderController.quantity = qty; // Store the valid quantity
                    orderController.badge = badge; // Associate the badge

                    // Close the popup stage only if it exists
                    if (popupStage != null) {
                        popupStage.close();
                    }
                }
            } catch (NumberFormatException e) {
                orderController.quantity = BigDecimal.valueOf(-1);
                showErrorDialog("Invalid Input", "Please enter a valid number for quantity.");
            }
        });
    }

}
