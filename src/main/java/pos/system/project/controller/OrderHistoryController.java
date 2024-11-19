package pos.system.project.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Amil Srinath
 */
import pos.system.project.entity.Order;
import pos.system.project.entity.tm.OrderHistoryTM;
import pos.system.project.service.OrderHistoryService;
import pos.system.project.service.OrderService;
import pos.system.project.service.impl.OrderHistoryServiceImpl;
import pos.system.project.service.impl.OrderServiceImpl;

public class OrderHistoryController {
    @FXML
    public Text lblRowCount;

    @FXML
    public CheckBox checkCustomerID;

    @FXML
    public CheckBox checkOrderID;

    @FXML
    public CheckBox checkCustomerName;

    @FXML
    private TableColumn<Order, String> colPaid;

    @FXML
    private TableColumn<Order, String> colCustomerID;

    @FXML
    private TableColumn<Order, String> colCustomerName;

    @FXML
    private TableColumn<Order, String> colDate;

    @FXML
    private TableColumn<Order, String> colOrderID;

    @FXML
    private TableColumn<Order, String> colTotal;

    @FXML
    private TableColumn<Order, String> colUserID;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private TableView<Order> tblOrderHistory;

    @FXML
    private TextField txtSearch;

    @FXML
    public Button btnBack;

    OrderService orderService = new OrderServiceImpl();
    ObservableList<Order> observableList;

    public void initialize() throws IOException {
        datePickerFrom.setValue(LocalDate.now());
        datePickerTo.setValue(LocalDate.now());
        setDateFilter(LocalDate.now(), LocalDate.now());
    }

    public void setDateFilter(LocalDate from, LocalDate to) throws IOException {
        observableList = FXCollections.observableArrayList();

        colOrderID.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        // Extract customer ID as a String
        colCustomerID.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleStringProperty(order.getCustomer() != null ?
                    String.valueOf(order.getCustomer().getCusId()) : "-");
        });

        colCustomerName.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleStringProperty(order.getCustomer() != null ?
                    order.getCustomer().getCusName() : "-");
        });

        colDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Extract user ID as a String
        colUserID.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleStringProperty(order.getUser() != null ?
                    String.valueOf(order.getUser().getUserId()) : "N/A");
        });

        colPaid.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            return new SimpleStringProperty(order.getIsPaid() == 1 ?
                    "Paid" : "Not Paid");
        });

        // Get orders from service with date conversion (handled in the service method)
        List<Order> orderList = orderService.getOrdersByDateRange(from, to);
        observableList = FXCollections.observableArrayList(orderList);
        tblOrderHistory.setItems(observableList);
        lblRowCount.setText("Row Count : " + observableList.size());
    }

    @FXML
    public void btnBackOnAction(ActionEvent actionEvent) {
        btnBack.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();

            HomeController controller = loader.getController();
            controller.setUsername();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void SearchOnKeyReleased(KeyEvent keyEvent) {
        String searchText = txtSearch.getText().toLowerCase();

        if (searchText.isEmpty()) {
            tblOrderHistory.setItems(observableList); // Reset to all items when search text is empty
            lblRowCount.setText("Row Count : " + observableList.size()); // Update row count
            return;
        }

        ObservableList<Order> filteredList = FXCollections.observableArrayList();
        for (Order order : observableList) {
            boolean matches = false;
            String orderId = String.valueOf(order.getOrderId()).toLowerCase();
            String customerId = order.getCustomer() != null ? String.valueOf(order.getCustomer().getCusId()).toLowerCase() : "";
            String customerName = order.getCustomer() != null && order.getCustomer().getCusName() != null ? order.getCustomer().getCusName().toLowerCase() : "";

            // Match logic
            if (checkOrderID.isSelected() || checkCustomerID.isSelected() || checkCustomerName.isSelected()) {
                // Only match if the corresponding checkbox is selected
                if (checkOrderID.isSelected() && orderId.contains(searchText)) {
                    matches = true;
                }
                if (checkCustomerID.isSelected() && customerId.contains(searchText)) {
                    matches = true;
                }
                if (checkCustomerName.isSelected() && customerName.contains(searchText)) {
                    matches = true;
                }
            } else {
                // No checkboxes selected, search all fields
                if (orderId.contains(searchText) || customerId.contains(searchText) || customerName.contains(searchText)) {
                    matches = true;
                }
            }

            // Add to filtered list if any conditions matched
            if (matches) {
                filteredList.add(order);
            }
        }

        tblOrderHistory.setItems(filteredList);
        lblRowCount.setText("Row Count : " + filteredList.size()); // Update row count
    }


    @FXML
    public void datePickerFrom(ActionEvent actionEvent) throws IOException {
        setDateFilter(datePickerFrom.getValue(), datePickerTo.getValue());
    }

    @FXML
    public void datePickerTo(ActionEvent actionEvent) throws IOException {
        setDateFilter(datePickerFrom.getValue(), datePickerTo.getValue());
    }
}
