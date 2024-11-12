package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import pos.system.project.entity.tm.OrderHistoryTM;
import pos.system.project.service.OrderHistoryService;
import pos.system.project.service.impl.OrderHistoryServiceImpl;

public class OrderHistoryController {

    @FXML
    private TableColumn<?, ?> colCustomerID;

    @FXML
    private TableColumn<?, ?> colCustomerName;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colOrderID;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUserID;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private TableView<?> tblOrderHistory;

    @FXML
    private TextField txtSearch;

    @FXML
    public Button btnBack;

    List<OrderHistoryTM> orderHistoryList;
    OrderHistoryService orderHistoryService = new OrderHistoryServiceImpl();

    public void initialize() throws IOException {
        getAllOrderHistory();
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

    public void getAllOrderHistory() {
        orderHistoryList = orderHistoryService.getAllOrderHistory();
    }
}
