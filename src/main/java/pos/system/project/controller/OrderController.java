package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Item;
import pos.system.project.entity.tm.OrderTM;
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
    public TableView<OrderTM> tblOrder;
    ItemService itemService = new ItemServiceImpl();
    BadgeService badgeService = new BadgeServiceImpl();

    public static List<Item> itemList;
    public static List<Badge> badgeList;

    @FXML
    private TableColumn<OrderTM, Void> colAction;
    @FXML
    private TableColumn<?, ?> colBarcode;
    @FXML
    private TableColumn<?, ?> colItemName;
    @FXML
    private TableColumn<?, ?> colQuantity;
    @FXML
    private TableColumn<?, ?> colSubTotal;
    @FXML
    private TableColumn<?, ?> colUnitPrice;
    @FXML
    public TextField itemBarcode;
    @FXML
    public Button btnBack;
    @FXML
    public Label lblTotal;

    public double quantity = 0;
    public Badge badge;

    public void initialize() throws IOException {
        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();

        colBarcode.setCellValueFactory(new PropertyValueFactory<>("itemBarcode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colAction.setCellFactory(getDeleteButtonCellFactory());
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
        boolean itemFound = false;
        for (Item item : itemList) {
            if (item.getItemBarcode().equals(itemBarcode.getText())) {
                itemFound = true;

                // Handle badge logic here
                List<Badge> itemBadges = badgeList.stream()
                        .filter(badge -> badge.getItem().getItemId() == item.getItemId())
                        .toList();

                if (itemBadges.size() == 1) {
                    Badge singleBadge = itemBadges.get(0);
                    if (singleBadge.getStatus() == 1) {
                        PopupController popupController = new PopupController();
                        popupController.setOrderController(this, item.getItemId());
                        popupController.showQuantityInputDialog(singleBadge, null);

                        addRowTable(singleBadge, item);
                        itemBarcode.clear();
                    } else {
                        showErrorDialog("Inactive Badge", "This badge is not active.");
                    }
                } else if (itemBadges.size() > 1) {
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

    private void addRowTable(Badge badge, Item item) {
        if (quantity == -1){
            return;
        }

        for (OrderTM orderTM : tblOrder.getItems()) {
            if (orderTM.getItemBarcode().equals(item.getItemBarcode()) && orderTM.getUnitPrice() == badge.getSellingPrice()) {
                double qty = orderTM.getQuantity();
                qty += quantity;
                orderTM.setQuantity(qty);
                orderTM.setSubTotal(badge.getSellingPrice()*qty);
                tblOrder.refresh();
                itemBarcode.clear();
                setLblTotal();
                return;
            }
        }

        System.out.println(quantity);

        OrderTM orderTM = new OrderTM();
        orderTM.setItemBarcode(item.getItemBarcode());
        orderTM.setItemName(item.getItemName());
        orderTM.setQuantity(quantity);
        orderTM.setUnitPrice(badge.getSellingPrice());
        orderTM.setSubTotal(badge.getSellingPrice()*quantity);
        tblOrder.getItems().add(orderTM);
        itemBarcode.clear();
        setLblTotal();
        System.out.println(quantity);
    }

    public void setLblTotal() {
        double total = 0.0;
        for (OrderTM orderTM : tblOrder.getItems()) {
            total += orderTM.getSubTotal();
        }
        lblTotal.setText(String.format("%.2f", total));
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setData() {
        itemBarcode.requestFocus();
    }

    private Callback<TableColumn<OrderTM, Void>, TableCell<OrderTM, Void>> getDeleteButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<OrderTM, Void> call(final TableColumn<OrderTM, Void> param) {
                final TableCell<OrderTM, Void> cell = new TableCell<>() {

                    private final Button deleteButton = new Button();

                    {
                        // Load the delete icon image
                        Image deleteIcon = new Image(getClass().getResourceAsStream("/assest/icon/icons8-delete-100.png"));
                        ImageView imageView = new ImageView(deleteIcon);
                        imageView.setFitHeight(20); // Set desired height
                        imageView.setFitWidth(20);  // Set desired width
                        deleteButton.setGraphic(imageView); // Set the ImageView as the button's graphic

                        // Button action to delete the row
                        deleteButton.setOnAction((ActionEvent event) -> {
                            OrderTM selectedOrder = getTableView().getItems().get(getIndex());
                            tblOrder.getItems().remove(selectedOrder);
                            setLblTotal();
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                        setLblTotal();
                    }
                };
                setLblTotal();
                return cell;
            }
        };
    }
}
