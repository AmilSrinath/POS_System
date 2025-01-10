package pos.system.project.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pos.system.project.dto.CustomerDTO;
import pos.system.project.dto.OrderDTO;
import pos.system.project.dto.OrderDetailsDTO;
import pos.system.project.entity.*;
import pos.system.project.entity.tm.OrderTM;
import pos.system.project.service.*;
import pos.system.project.service.impl.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    public double unitePrice;
    public boolean isLeter;
    public double milliliters;
    public int status;
    public String type;
    public AnchorPane orderForm;
    @FXML
    public Button btnAddNewItem;

    ItemService itemService = new ItemServiceImpl();
    BadgeService badgeService = new BadgeServiceImpl();
    ShortCutService shortCutService = new ShortCutServiceImpl();
    OrderService orderService = new OrderServiceImpl();
    CustomerService customerService = new CustomerSerivceImpl();
    CustomerLoanService customerLoanService = new CustomerLoanServiceImpl();

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
    public TableColumn<?, ?> colType;
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

    TextField txtMobileNumber = new TextField();
    Label lblCustomerName = new Label();

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
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory<>("subTotal"));
        colAction.setCellFactory(getDeleteButtonCellFactory());

        orderForm.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F12:
                    try {
                        PlaceOrderOnAction(new ActionEvent(event.getSource(), event.getTarget()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case F9:
                    addOtherItems();
                default:
                    // Handle other key events if needed
                    break;
            }
        });

        setupAutoSuggestion();
        shortCut();
        setImageForShortCuts();
    }

    Customer customerByMobileNumber = null;

    private void setImageForShortCuts() throws IOException {
        List<ShortCut> shortCuts = shortCutService.getAllShortCuts();
        ImageView[] imageViews = {imgF1, imgF2, imgF3, imgF4, imgF5, imgF6, imgF7, imgF8};

        for (int i = 0; i < shortCuts.size() && i < imageViews.length; i++) {
            ShortCut shortCut = shortCuts.get(i);
            String itemBarcode = shortCutService.getItemBarCodeById(shortCut.getShortcutId());

            for (Item item : itemList) {
                if (item.getItemBarcode().equals(itemBarcode)) {
                    String base64Image = item.getImageUrl();

                    try {
                        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(bis);

                        imageViews[i].setImage(image);
                        break;
                    }catch (NullPointerException e){}
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
                .filter(item -> item.getStatus() == 1)
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

    public Customer customerByMobileNumber1 = null;

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
        Dialog<String> customDialog = new Dialog<>();
        customDialog.setTitle("Enter Quantity");

        // Custom label
        Text label = new Text("Enter Amount:");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000;");

        // Text input field
        TextField inputField = new TextField();
        inputField.setStyle("-fx-font-size: 20px;");

        VBox vbox = new VBox(label, inputField);
        vbox.setSpacing(10);

        customDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        customDialog.getDialogPane().setContent(vbox);

        customDialog.setOnShown(dialogEvent -> Platform.runLater(inputField::requestFocus));
        customDialog.showAndWait();

        double cash = 0.0;
        double amount = 0.0;
        boolean isPaid = false;

        if (inputField.getText().isEmpty()) {
            // Handle case when the input field is empty
            Dialog<String> mobileNumberDialog = new Dialog<>();

            TextField txtMobileNumber = new TextField();
            txtMobileNumber.setPromptText("Enter Mobile Number");
            txtMobileNumber.setStyle("-fx-font-size: 22px;");

            Label lblCustomerName = new Label();
            lblCustomerName.setStyle("-fx-font-size: 22px;");

            Label lblCustomerMobileNumber = new Label("Customer Mobile Number:");
            lblCustomerMobileNumber.setStyle("-fx-font-size: 22px;");

            VBox vboxContent = new VBox(10);
            vboxContent.setAlignment(Pos.CENTER);
            vboxContent.getChildren().addAll(lblCustomerMobileNumber, txtMobileNumber, lblCustomerName);

            mobileNumberDialog.getDialogPane().setContent(vboxContent);

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            mobileNumberDialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            txtMobileNumber.setOnKeyReleased(event -> {
                try {
                    Customer customer = customerService.getCustomerByMobileNumber(txtMobileNumber.getText());
                    if (customer != null) {
                        lblCustomerName.setText(customer.getCusName() != null ? customer.getCusName() : "No Name");
                        lblCustomerName.setStyle("-fx-text-fill: red; -fx-font-size: 23px;");
                    } else {
                        lblCustomerName.setText("");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            Platform.runLater(() -> txtMobileNumber.requestFocus());

            mobileNumberDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    String enteredMobileNumber = txtMobileNumber.getText();
                    if (enteredMobileNumber.isEmpty()) {
                        lblCustomerName.setText("No customer found");
                        return null;
                    }

                    Customer customer = null;
                    try {
                        customer = customerService.getCustomerByMobileNumber(enteredMobileNumber);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (customer != null) {
                        CustomerLoan customerLoan = new CustomerLoan();
                        customerLoan.setLoanAmount(Double.parseDouble(lblTotal.getText()));
                        customerLoan.setStatus(1);
                        customerLoan.setCustomer(customer);
                        customerLoan.setCusMobileNumber(enteredMobileNumber);
                        customerLoan.setCusName(customer.getCusName());

                        customerByMobileNumber1 = customer;

                        try {
                            customerLoanService.saveLoan(customerLoan);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        CustomerDTO customerDTO = new CustomerDTO();
                        customerDTO.setCusPhone(enteredMobileNumber);
                        try {
                            customer = customerService.Add(customerDTO);
                            customerByMobileNumber1 = customer;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        CustomerLoan customerLoan = new CustomerLoan();
                        customerLoan.setLoanAmount(Double.parseDouble(lblTotal.getText()));
                        customerLoan.setStatus(1);
                        customerLoan.setCustomer(customer);
                        customerLoan.setCusMobileNumber(enteredMobileNumber);
                        customerLoan.setCusName(customer.getCusName());

                        try {
                            customerLoanService.saveLoan(customerLoan);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return enteredMobileNumber;
                }
                return null; // If Cancel button is pressed, return null and don't add the order
            });

            Optional<String> result = mobileNumberDialog.showAndWait();
            if (result.isEmpty()) {
                return; // If "Cancel" is clicked, we exit the method without saving the order
            }
        } else {
            // Process order if amount is provided
            amount = Double.parseDouble(inputField.getText());
            double tot = Double.parseDouble(lblTotal.getText());
            cash = amount - tot;
            isPaid = true;

            Dialog<String> cashDialog = new Dialog<>();
            Label cashLbl = new Label("Cash: " + cash);
            cashLbl.setStyle("-fx-text-fill: #31ca07; -fx-font-size: 30px; -fx-font-weight: bold;");

            VBox vboxCash = new VBox(cashLbl);
            vboxCash.setSpacing(50);
            vboxCash.setAlignment(Pos.CENTER);

            vboxCash.setPrefSize(300, 130);

            cashDialog.getDialogPane().setContent(vboxCash);
            cashDialog.getDialogPane().setPrefSize(350, 150);
            cashDialog.getDialogPane().setContent(vboxCash);

            cashDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            cashDialog.show();
        }

        itemBarcode.requestFocus();

        List<OrderTM> items = tblOrder.getItems();
        if (items.isEmpty()) {
            showErrorDialog("Error", "Please add item to order");
            return;
        }

        OrderDTO order = new OrderDTO();
        order.setTotal(Double.parseDouble(lblTotal.getText()));
        order.setAmountPaid(amount);
        order.setBalance(cash);
        order.setUser(HomeController.user);

        order.setCustomer(customerByMobileNumber1);
        order.setCustomerName(customerByMobileNumber1 != null ? customerByMobileNumber1.getCusName() : "Unknown");

        if (isPaid) {
            order.setIsPaid(1);
        } else {
            order.setIsPaid(0);
        }

        Order saveOrder = orderService.saveOrder(order);
        System.out.println("<><> : "+saveOrder);
        customerByMobileNumber1 = null;

        for (OrderTM item : items) {
            OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
            orderDetailsDTO.setOrder(saveOrder);
            orderDetailsDTO.setUser(HomeController.user);
            orderDetailsDTO.setSubTotal(item.getSubTotal());

            for (Badge badge : badgeList) {
                if (badge.getBadgeId() == item.getBadgeId()) {
                    orderDetailsDTO.setQuantity(item.getQuantity());
                    orderDetailsDTO.setItem(badge.getItem());
                    orderDetailsDTO.setItemPrice(item.getUnitPrice());
                    orderDetailsDTO.setBadgeId(badge.getBadgeId());
                    orderDetailsDTO.setItemType(item.getType());
                }
            }
            System.out.println("Get Order : "+orderDetailsDTO.getOrder());
            orderService.saveOrderDetails(orderDetailsDTO);
        }

        for (OrderTM item : items) {
            orderService.updateQuantity(item.getBadgeId(), item.getQuantity(), item.getMilliliter(), item.getStatus());
        }

        itemList = itemService.getAllItems();
        badgeList = badgeService.getAllBadges();
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

        if (item.getSellByStatus() == 1){
            for (OrderTM orderTM : tblOrder.getItems()) {
                if (orderTM.getBadgeId() == badge.getBadgeId()) {
                    double qty = orderTM.getQuantity();
                    BigDecimal qtyBigDecimal = BigDecimal.valueOf(qty);
                    qtyBigDecimal = qtyBigDecimal.add(quantity);
                    orderTM.setQuantity(qtyBigDecimal.doubleValue());
                    orderTM.setBadgeId(badge.getBadgeId());
                    orderTM.setType("unit");
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
            orderTM.setType("unit");
            orderTM.setUnitPrice(badge.getSellingPrice());
            BigDecimal sellingPrice = BigDecimal.valueOf(badge.getSellingPrice());
            BigDecimal subTotal = sellingPrice.multiply(quantity);
            orderTM.setSubTotal(subTotal.doubleValue());
            tblOrder.getItems().add(orderTM);
            itemBarcode.clear();
            setLblTotal();
            setQuantity();
        }else if (item.getSellByStatus() == 2) {
            for (OrderTM orderTM : tblOrder.getItems()) {
                if (orderTM.getBadgeId() == badge.getBadgeId()) {
                    double currentQty = orderTM.getQuantity();
                    BigDecimal currentQtyBigDecimal = BigDecimal.valueOf(currentQty);

                    BigDecimal totalQuantity = currentQtyBigDecimal.add(quantity);
                    BigDecimal quantity = totalQuantity.divide(BigDecimal.valueOf(1000));
                    orderTM.setQuantity(totalQuantity.doubleValue());
                    orderTM.setType("g");
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
            orderTM.setType("g");
            orderTM.setQuantity(quantity.doubleValue());
            orderTM.setUnitPrice(badge.getSellingPrice());
            orderTM.setBadgeId(badge.getBadgeId());

            //i want to divide the quantity by 1000
            quantity = quantity.divide(BigDecimal.valueOf(1000));
            orderTM.setSubTotal(badge.getSellingPrice()*quantity.doubleValue());

            tblOrder.getItems().add(orderTM);
            itemBarcode.clear();
            setLblTotal();
        } else if (item.getSellByStatus() == 3) {
            if (isLeter && milliliters > 0) {
                OrderTM orderTM = new OrderTM();
                orderTM.setItemBarcode(item.getItemBarcode());
                orderTM.setItemName(badge.getDescription());
                orderTM.setBadgeId(badge.getBadgeId());
                orderTM.setQuantity(quantity.doubleValue());
                orderTM.setUnitPrice(badge.getSellingPriceLiter());
                orderTM.setMilliliter(milliliters);
                orderTM.setStatus(status);
                orderTM.setType(type);
                BigDecimal sellingPrice = BigDecimal.valueOf(Math.round(badge.getSellingPriceLiter()));
                BigDecimal subTotal = sellingPrice.multiply(quantity);
                orderTM.setSubTotal(subTotal.doubleValue());
                tblOrder.getItems().add(orderTM);
            } else if (milliliters > 0) {
                OrderTM orderTM = new OrderTM();
                orderTM.setItemBarcode(item.getItemBarcode());
                orderTM.setItemName(badge.getDescription());
                orderTM.setBadgeId(badge.getBadgeId());
                orderTM.setQuantity(quantity.doubleValue());
                orderTM.setUnitPrice(badge.getSellingPrice());
                orderTM.setType(type);
                orderTM.setMilliliter(milliliters);
                orderTM.setStatus(status);
                BigDecimal sellingPrice = BigDecimal.valueOf(badge.getSellingPrice());
                BigDecimal subTotal = sellingPrice.multiply(quantity);
                orderTM.setSubTotal(subTotal.doubleValue());
                tblOrder.getItems().add(orderTM);
            }

            itemBarcode.clear();
            setLblTotal();
            setQuantity();
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

    @FXML
    public void btnAddNewItemOnAction(ActionEvent actionEvent) {
        addOtherItems();
    }

    public void addOtherItems() {
        TextInputDialog customDialog2 = new TextInputDialog();
        customDialog2.setTitle("Enter Price");
        customDialog2.setHeaderText("Enter Item Price: ");
        customDialog2.setContentText("Item Price : ");
        customDialog2.getDialogPane().setStyle("-fx-font-size: 22px;");
        Optional<String> result2 = customDialog2.showAndWait();
        result2.ifPresent(quantity -> {
            String s = result2.get();

            OrderTM orderTM = new OrderTM();
            orderTM.setItemName("Other");
            orderTM.setSubTotal(Double.parseDouble(s));
            tblOrder.getItems().add(orderTM);
        });
    }
}
