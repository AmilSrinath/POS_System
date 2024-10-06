package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pos.system.project.dto.OrderDTO;
import pos.system.project.dto.OrderDetailsDTO;
import pos.system.project.entity.*;
import pos.system.project.entity.tm.OrderTM;
import pos.system.project.service.BadgeService;
import pos.system.project.service.ItemService;
import pos.system.project.service.OrderService;
import pos.system.project.service.ShortCutService;
import pos.system.project.service.impl.BadgeServiceImpl;
import pos.system.project.service.impl.ItemServiceImpl;
import pos.system.project.service.impl.OrderServiceImpl;
import pos.system.project.service.impl.ShortCutServiceImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class OrderController {
    public TableView<OrderTM> tblOrder;
    public boolean isTrue = true;
    @FXML
    public Button btnF1;
    @FXML
    public Button btnF2;
    @FXML
    public Button btnF3;
    @FXML
    public Button btnF4;
    @FXML
    public Button btnF5;
    @FXML
    public Button btnF6;
    @FXML
    public Button btnF7;
    @FXML
    public Button btnF8;
    @FXML
    public ImageView imgF1;
    @FXML
    public ImageView imgF2;
    @FXML
    public ImageView imgF3;
    @FXML
    public ImageView imgF4;
    @FXML
    public ImageView imgF5;
    @FXML
    public ImageView imgF6;
    @FXML
    public ImageView imgF7;
    @FXML
    public ImageView imgF8;

    ItemService itemService = new ItemServiceImpl();
    BadgeService badgeService = new BadgeServiceImpl();
    ShortCutService shortCutService = new ShortCutServiceImpl();
    OrderService orderService = new OrderServiceImpl();

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

    private ContextMenu suggestionMenu;

    public BigDecimal quantity = BigDecimal.valueOf(0);
    public Badge badge;
    public static int shortCutID;

    public void initialize() throws IOException {
        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();

        colBarcode.setCellValueFactory(new PropertyValueFactory<>("itemBarcode"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colAction.setCellFactory(getDeleteButtonCellFactory());

        setupAutoSuggestion();
        shortCut();
        setImageForShortCuts();
    }

    private void setImageForShortCuts() throws IOException {
        List<ShortCut> shortCuts = shortCutService.getAllShortCuts();
        ImageView[] imageViews = {imgF1, imgF2, imgF3, imgF4, imgF5, imgF6, imgF7, imgF8};

        for (int i = 0; i < shortCuts.size() && i < imageViews.length; i++) {
            ShortCut shortCut = shortCuts.get(i);
            String itemBarcode = shortCutService.getItemBarCodeById(shortCut.getShortcutId());

            for (Item item : itemList) {
                if (item.getItemBarcode().equals(itemBarcode)) {
                    String base64Image = item.getImageUrl();

                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                    Image image = new Image(bis);

                    imageViews[i].setImage(image);
                    break;
                }
            }
        }
    }

    private void setFunctionKey(int num) {
        try {
            itemBarcode.setText(shortCutService.getItemBarCodeById(num));
        } catch (IOException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            showErrorDialog("Error", "This key is not item. Please add item first");
        }
        itemBarcode.requestFocus();
        itemBarcode.fireEvent(
                new KeyEvent(KeyEvent.KEY_PRESSED, KeyCode.ENTER.toString(), "", KeyCode.ENTER, false, false, false, false)
        );
    }

    private void shortCut() {
        itemBarcode.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F1:
                    setFunctionKey(1);
                    break;

                case F2:
                    setFunctionKey(2);
                    break;

                case F3:
                    setFunctionKey(3);
                    break;

                case F4:
                    setFunctionKey(4);
                    break;

                case F5:
                    setFunctionKey(5);
                    break;

                case F6:
                    setFunctionKey(6);
                    break;

                case F7:
                    setFunctionKey(7);
                    break;

                case F8:
                    setFunctionKey(8);
                    break;
            }
        });
    }

    private void showShortCutDialog() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/shortcut.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupAutoSuggestion() {
        suggestionMenu = new ContextMenu();
        itemBarcode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                suggestionMenu.hide();
            } else {
                List<MenuItem> suggestions = getSuggestions(newValue);
                if (!suggestions.isEmpty()) {
                    suggestionMenu.getItems().setAll(suggestions);
                    if (!suggestionMenu.isShowing()) {
                        suggestionMenu.show(itemBarcode, Side.BOTTOM, 0, 0);
                    }
                } else {
                    suggestionMenu.hide();
                }
            }
        });

        itemBarcode.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                suggestionMenu.hide();
            }
        });
    }

    private List<MenuItem> getSuggestions(String query) {
        List<MenuItem> suggestions = itemList.stream()
                .filter(item -> item.getItemName().toLowerCase().contains(query.toLowerCase()))
                .map(item -> {
                    MenuItem suggestion = new MenuItem(item.getItemName());
                    suggestion.setOnAction(e -> {
                        itemBarcode.setText(item.getItemBarcode()); // Set barcode when name is selected
                        suggestionMenu.hide();
                    });
                    return suggestion;
                }).toList();
        return suggestions;
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

    public void PlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        List<OrderTM> items = tblOrder.getItems();

        if (items.isEmpty()) {
            showErrorDialog("Error", "Please add item to order");
            return;
        }

        OrderDTO order = new OrderDTO();
        order.setTotal(Double.parseDouble(lblTotal.getText()));
        order.setUser(HomeController.user);
        order.setCustomer(null);

        Order saveOrder = orderService.saveOrder(order);

        for (OrderTM item : items) {
            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();

            for (Badge badge : badgeList) {
                if (badge.getBadgeId() == item.getBadgeId()) {
                    orderDetailsDTO.setQuantity((int) item.getQuantity());
                    orderDetailsDTO.setOrder(saveOrder);
                    orderDetailsDTO.setUser(HomeController.user);
                    orderDetailsDTO.setItem(badge.getItem());
                    orderDetailsDTO.setSubTotal(item.getSubTotal());
                    orderDetailsDTO.setItemPrice(item.getUnitPrice());
                    orderDetailsDTO.setBadgeId(badge.getBadgeId());
                }
            }

            orderService.saveOrderDetails(orderDetailsDTO);
        }

        for (OrderTM item : items) {
            orderService.updateQuantity(item.getBadgeId(), (int) item.getQuantity());
        }

        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();

        //clear
//        tblOrder.getItems().clear();
//        lblTotal.setText("0.00");
    }

    @FXML
    public void itemBarcodeOnAction(ActionEvent actionEvent) throws IOException {
        boolean itemFound = false;
        for (Item item : itemList) {
            if (item.getItemBarcode().equals(itemBarcode.getText())) {
                itemFound = true;

                List<Badge> itemBadges = badgeList.stream()
                        .filter(badge -> badge.getItem().getItemId() == item.getItemId() && badge.getStatus() == 1)
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

                        popupStage.getScene().setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.ESCAPE) {
                                popupStage.close(); // Close the stage when "Esc" is pressed
                                itemBarcode.clear();
                            }
                        });

                        popupStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        showErrorDialog("Error", "Failed to load the popup. Please try again.");
                        itemBarcode.clear();
                    }
                } else {
                    showErrorDialog("No Badges Found", "This item has no active badges.");
                    itemBarcode.clear();
                }
            }
        }

        if (!itemFound) {
            showErrorDialog("Invalid Barcode", "No item found for the entered barcode.");
            itemBarcode.clear();
        }
    }

    public void addRowTable(Badge badge, Item item) throws IOException {
        if (quantity.compareTo(BigDecimal.valueOf(-1)) == 0 || !isTrue){
            return;
        }

        if (item.getSellByStatus() == 1) {
            for (OrderTM orderTM : tblOrder.getItems()) {
                if (orderTM.getBadgeId() == badge.getBadgeId()) {
                    double qty = orderTM.getQuantity();
                    BigDecimal qtyBigDecimal = BigDecimal.valueOf(qty);
                    qtyBigDecimal = qtyBigDecimal.add(quantity);
                    orderTM.setQuantity(qtyBigDecimal.doubleValue());
                    orderTM.setBadgeId(badge.getBadgeId());
                    orderTM.setSubTotal(badge.getSellingPrice()*qtyBigDecimal.doubleValue());
                    tblOrder.refresh();
                    itemBarcode.clear();
                    setLblTotal();
                    setQuantity();
                    return;
                }
            }

            OrderTM orderTM = new OrderTM();
            orderTM.setItemBarcode(item.getItemBarcode());
            orderTM.setItemName(badge.getDescription());
            orderTM.setBadgeId(badge.getBadgeId());
            orderTM.setQuantity(quantity.doubleValue());
            orderTM.setUnitPrice(badge.getSellingPrice());
            BigDecimal sellingPrice = BigDecimal.valueOf(badge.getSellingPrice());
            BigDecimal subTotal = sellingPrice.multiply(quantity);
            orderTM.setSubTotal(subTotal.doubleValue());
            tblOrder.getItems().add(orderTM);
            itemBarcode.clear();
            setLblTotal();
            setQuantity();
        }else {
            for (OrderTM orderTM : tblOrder.getItems()) {
                if (orderTM.getBadgeId() == badge.getBadgeId()) {
                    double currentQty = orderTM.getQuantity();
                    BigDecimal currentQtyBigDecimal = BigDecimal.valueOf(currentQty);

                    BigDecimal totalQuantity = currentQtyBigDecimal.add(quantity);
                    BigDecimal quantity = totalQuantity.divide(BigDecimal.valueOf(1000));
                    orderTM.setQuantity(totalQuantity.doubleValue());

                    orderTM.setSubTotal(badge.getSellingPrice() * quantity.doubleValue());
                    tblOrder.refresh();
                    itemBarcode.clear();
                    setLblTotal();

                    setQuantity();
                    return;
                }
            }
            OrderTM orderTM = new OrderTM();
            orderTM.setItemBarcode(item.getItemBarcode());
            orderTM.setItemName(badge.getDescription());
            orderTM.setQuantity(quantity.doubleValue());
            orderTM.setUnitPrice(badge.getSellingPrice());
            orderTM.setBadgeId(badge.getBadgeId());

            //i want to divide the quantity by 1000
            quantity = quantity.divide(BigDecimal.valueOf(1000));
            orderTM.setSubTotal(badge.getSellingPrice()*quantity.doubleValue());

            tblOrder.getItems().add(orderTM);
            itemBarcode.clear();
            setLblTotal();
        }
        setQuantity();
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
                        Image deleteIcon = new Image(getClass().getResourceAsStream("/assest/icon/icons8-delete-100.png"));
                        ImageView imageView = new ImageView(deleteIcon);
                        imageView.setFitHeight(20);
                        imageView.setFitWidth(20);
                        deleteButton.setGraphic(imageView);

                        deleteButton.setOnAction((ActionEvent event) -> {
                            OrderTM selectedOrder = getTableView().getItems().get(getIndex());
                            tblOrder.getItems().remove(selectedOrder);
                            restoreQuantity(selectedOrder);

                            setLblTotal();
                            try {
                                setQuantity();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
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

    private void restoreQuantity(OrderTM orderTM) {
        String barcode = orderTM.getItemBarcode();
        BigDecimal removedQuantity = BigDecimal.valueOf(orderTM.getQuantity());

        List<Badge> badges = badgeList.stream()
                .filter(badge -> badge.getItem().getItemBarcode().equals(barcode))
                .toList();

        for (Badge badge : badges) {
            BigDecimal updatedQuantity = badge.getQuantity().add(removedQuantity);
            badge.setQuantity(updatedQuantity);
            break;
        }
    }

    public void setQuantity() throws IOException {
        badgeList = badgeService.getAllBadges();

        for (OrderTM orderTM : tblOrder.getItems()) {
            int orderBadgeId = orderTM.getBadgeId(); // Get badgeId from the order row
            BigDecimal orderedQuantity = BigDecimal.valueOf(orderTM.getQuantity());

            Badge badge = badgeList.stream()
                    .filter(b -> b.getBadgeId() == orderBadgeId)
                    .findFirst()
                    .orElse(null);

            if (badge != null) {
                BigDecimal availableQuantity = badge.getQuantity();
                BigDecimal updatedQuantity = availableQuantity.subtract(orderedQuantity);

                if (updatedQuantity.compareTo(BigDecimal.ZERO) < 0) {
                    showErrorDialog("Insufficient Stock",
                            "Not enough stock for Badge ID: " + orderBadgeId + ". Available: " + availableQuantity);
                    return;
                } else {
                    badge.setQuantity(updatedQuantity);
                }
            } else {
                showErrorDialog("Badge Not Found", "No badge found with ID: " + orderBadgeId);
            }
        }
    }

    @FXML
    public void F1OnAction(ActionEvent actionEvent) {
        shortCutID = 1;
        showShortCutDialog();
    }

    @FXML
    public void F2OnAction(ActionEvent actionEvent) {
        shortCutID = 2;
        showShortCutDialog();
    }

    @FXML
    public void F3OnAction(ActionEvent actionEvent) {
        shortCutID = 3;
        showShortCutDialog();
    }

    @FXML
    public void F4OnAction(ActionEvent actionEvent) {
        shortCutID = 4;
        showShortCutDialog();
    }

    @FXML
    public void F5OnAction(ActionEvent actionEvent) {
        shortCutID = 5;
        showShortCutDialog();
    }

    @FXML
    public void F6OnAction(ActionEvent actionEvent) {
        shortCutID = 6;
        showShortCutDialog();
    }

    @FXML
    public void F7OnAction(ActionEvent actionEvent) {
        shortCutID = 7;
        showShortCutDialog();
    }

    @FXML
    public void F8OnAction(ActionEvent actionEvent) {
        shortCutID = 8;
        showShortCutDialog();
    }

}
