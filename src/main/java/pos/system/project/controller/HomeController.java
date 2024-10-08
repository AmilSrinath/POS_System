package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos.system.project.entity.User;
import pos.system.project.service.UserService;
import pos.system.project.service.impl.UserServiceImpl;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class HomeController {
    public static User user;

    @FXML
    public AnchorPane home;

    @FXML
    public Button btnCategory;

    @FXML
    public Label lblUsername;

    @FXML
    private Button btnConfiguration;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnEmployees;

    @FXML
    private Button btnItems;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnPayments;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnUsers;

    private final Stage stage = new Stage();
    UserService userService = new UserServiceImpl();

    @FXML
    void btnConfigurationOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/configuration.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnCustomersOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnEmployeesOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/employee.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnItemsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/item.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {

    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order.fxml"));
        AnchorPane anchorPane = loader.load();

        OrderController orderController = loader.getController();
        orderController.setData();

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnPaymentsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/payment.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnSettingsOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/setting.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    void btnUsersOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    public void btnReportsOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/report.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    @FXML
    public void btnCategoryOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/category.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        home.getScene().getWindow().hide();
    }

    public void setUserID(String username) throws IOException {
        user = userService.getUserByUsername(username);
        lblUsername.setText(user.getUsername());
    }

    public void setUsername(){
        lblUsername.setText(user.getUsername());
    }

}
