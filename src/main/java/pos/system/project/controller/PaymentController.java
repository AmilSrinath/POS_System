package pos.system.project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pos.system.project.entity.Customer;
import pos.system.project.entity.tm.CustomerLoanTM;
import pos.system.project.service.CustomerLoanService;
import pos.system.project.service.impl.CustomerLoanServiceImpl;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class PaymentController {
    public TableView<CustomerLoanTM> tblCustomerLoan;
    CustomerLoanService customerLoanService = new CustomerLoanServiceImpl();
    List<CustomerLoanTM> customerLoanTMS;
    ObservableList<CustomerLoanTM> observableList;

    @FXML
    public TableView<CustomerLoanTM> tblUser;
    @FXML
    public TableColumn<?, ?> colName;
    @FXML
    public TableColumn<?, ?> colMobileNo;
    @FXML
    public TableColumn<?, ?> colTotalAmount;

    @FXML
    public Button btnBack;

    @FXML
    public void initialize() throws IOException {
        customerLoanTMS = customerLoanService.getAllLoans();
        getAllLoans();
    }

    public void getAllLoans() throws IOException {
        customerLoanTMS = customerLoanService.getAllLoans();
        observableList = FXCollections.observableArrayList();

        for (CustomerLoanTM customerLoanTM : customerLoanTMS) {
            observableList.add(customerLoanTM);
        }
        tblCustomerLoan.setItems(observableList);

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMobileNo.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
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

    @FXML
    public void tableOnMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            CustomerLoanTM customerLoanTM = tblCustomerLoan.getSelectionModel().getSelectedItem();
        }
    }
}
