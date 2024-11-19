package pos.system.project.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.hibernate.exception.ConstraintViolationException;
import pos.system.project.dto.CustomerDTO;
import pos.system.project.entity.Customer;
import pos.system.project.service.CustomerService;
import pos.system.project.service.impl.CustomerSerivceImpl;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Amil Srinath
 */
public class CustomerController {
    CustomerService customerService = new CustomerSerivceImpl();
    public static List<Customer> customerList;
    ObservableList<Customer> observableList;
    int currentSelectedId = -1;

    @FXML
    public TableView<Customer> tblCustomer;
    @FXML
    public TableColumn<?, ?> colCusName;
    @FXML
    public TableColumn<?, ?> colCusPhone;
    @FXML
    public TableColumn<?, ?> colCusAddress;
    @FXML
    public TableColumn<?, ?> colCusNIC;
    @FXML
    public TableColumn<?, ?> colCusDOB;
    @FXML
    public Label lblTime;
    @FXML
    public Label lblDate;
    @FXML
    public TextField cusName;
    @FXML
    public TextField cusPhone;
    @FXML
    public TextArea cusAddress;
    @FXML
    public TextField cusNIC;
    @FXML
    public DatePicker cusDOB;
    @FXML
    public Button btnBack;

    public void initialize() throws IOException {
        TimeNow();
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        getAllCustomers();
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

    public void AddOnAction(ActionEvent actionEvent) throws IOException {
        for(Customer customer : customerList){
            if(customer.getCusPhone().equals(cusPhone.getText())){
                new Alert(Alert.AlertType.ERROR, "Duplicated Phone...!").show();
                return;
            }

            if(customer.getNic().equals(cusNIC.getText())){
                new Alert(Alert.AlertType.ERROR, "Duplicated NIC...!").show();
                return;
            }
        }

        try {
             customerService.Add(
                    new CustomerDTO(
                            cusName.getText(),
                            cusPhone.getText(),
                            cusAddress.getText(),
                            cusNIC.getText(),
                            cusDOB.getValue()
                    )
            );
            getAllCustomers();
        }catch(ConstraintViolationException e){
            new Alert(Alert.AlertType.ERROR, "Duplicated Entry...!").show();
        }
    }

    public void EditOnAction(ActionEvent actionEvent) throws IOException {
        if (currentSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select a customer...!").show();
            return;
        }

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to edit?", yes, no).showAndWait();

        try {
            if (result.orElse(no) == yes) {
                customerService.Edit(
                        new CustomerDTO(
                                cusName.getText(),
                                cusPhone.getText(),
                                cusAddress.getText(),
                                cusNIC.getText(),
                                cusDOB.getValue()
                        ), currentSelectedId
                );
            }
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        ClearOnAction(actionEvent);
        getAllCustomers();
        currentSelectedId = -1;
    }

    public void DeleteOnAction(ActionEvent actionEvent) throws IOException {
        if (currentSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select a customer...!").show();
            return;
        }

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            customerService.Delete(currentSelectedId);
            ClearOnAction(actionEvent);
        }
        getAllCustomers();
        currentSelectedId = -1;
    }

    public void getAllCustomers() throws IOException {
        customerList = customerService.getAllCustomers();
        observableList = FXCollections.observableArrayList();

        for (Customer customer : customerList) {
            if (customer.getStatus() == 1) {
                observableList.add(customer);
            }
        }
        tblCustomer.setItems(observableList);

        colCusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        colCusPhone.setCellValueFactory(new PropertyValueFactory<>("cusPhone"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        colCusNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCusDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
    }

    @FXML
    public void tableOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            Customer customer = tblCustomer.getSelectionModel().getSelectedItem();
            Customer customerByMobileNumber = customerService.getCustomerByMobileNumber(customer.getCusPhone());

            cusName.setText(customerByMobileNumber.getCusName());
            cusPhone.setText(customerByMobileNumber.getCusPhone());
            cusAddress.setText(customerByMobileNumber.getCusAddress());
            cusNIC.setText(customerByMobileNumber.getNic());

            try {
                cusDOB.setValue(customerByMobileNumber.getDob());
            }catch (Exception e){}
            currentSelectedId = customerByMobileNumber.getCusId();
            System.out.println(customerByMobileNumber.getCusId());
        }
    }

    public void ClearOnAction(ActionEvent actionEvent) {
        cusName.clear();
        cusPhone.clear();
        cusAddress.clear();
        cusNIC.clear();
        cusDOB.setValue(null);
    }

    private void TimeNow(){
        Thread thread = new Thread(() ->{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while (!false){
                try {
                    Thread.sleep(1000);
                }catch (Exception e){}
                final String timenow = sdf.format(new Date());
                Platform.runLater(() ->{
                    lblTime.setText(timenow);
                });
            }
        });
        thread.start();
    }
}
