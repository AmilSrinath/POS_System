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
                selectBadge(cardList.get(selectedIndex), popupStage); // Select the current card
            }
        });

        popupStage.show(); // Show the popup with badges
    }

    private void highlightSelectedCard() {
        if (selectedIndex >= 0 && selectedIndex < cardList.size()) {
            // Deselect the current card (if any)
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

    private void selectBadge(AnchorPane selectedCard, Stage popupStage) {
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
        }
    }

    private AnchorPane createBadgeCard(Badge badge, Stage popupStage, OrderController orderController) {
        AnchorPane card = new AnchorPane();
        card.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #ccc;");
        card.setPrefSize(600, 100);

        Label badgeIdLabel = new Label("Badge ID: " + badge.getBadgeId());
        badgeIdLabel.setLayoutX(20);
        badgeIdLabel.setLayoutY(20);

        Label badgeStatusLabel = new Label("Status: " + (badge.getStatus() == 1 ? "Active" : "Inactive"));
        badgeStatusLabel.setLayoutX(20);
        badgeStatusLabel.setLayoutY(50);

        Label quantityLabel = new Label("Quantity: " + badge.getQuantity());
        quantityLabel.setLayoutX(20);
        quantityLabel.setLayoutY(80);

        Label priceLabel = new Label("Selling Price: " + badge.getSellingPrice());
        priceLabel.setLayoutX(300);
        priceLabel.setLayoutY(20);

        Label expiryDateLabel = new Label("Expiry Date: " + badge.getExpireDate().toString());
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
        card.setOnMouseClicked(event -> selectBadge(card, popupStage));

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
        dialog.setTitle("Enter Quantity");
        dialog.setHeaderText("Enter the quantity for Badge ID: " + badge.getBadgeId()+"\nAvailable Stock: " + badge.getQuantity());
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
                    orderController.itemBarcode.setText(String.valueOf(qty));
                    // Close the popup stage only if it exists
                    if (popupStage != null) {
                        popupStage.close();
                    }
                }
            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Please enter a valid number for quantity.");
            }
        });
    }

}
