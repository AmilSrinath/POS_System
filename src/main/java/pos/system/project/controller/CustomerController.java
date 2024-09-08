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
    public TableColumn<?, ?> colCusPhone1;
    @FXML
    public TableColumn<?, ?> colCusPhone2;
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
    public TextField cusPhone1;
    @FXML
    public TextField cusPhone2;
    @FXML
    public TextArea cusAddress;
    @FXML
    public TextField cusNIC;
    @FXML
    public DatePicker cusDOB;
    @FXML
    public Button btnBack;

    public void initialize() throws IOException {
        setTime();
        setDate();
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

    private void setTime(){
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

    private void setDate() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            while (!false) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
                final String datenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    lblDate.setText(datenow);
                });
            }
        });
        thread.start();
    }

    public void AddOnAction(ActionEvent actionEvent) throws IOException {
        try {
            int status = customerService.Add(
                    new CustomerDTO(
                            cusName.getText(),
                            cusPhone1.getText(),
                            cusPhone2.getText(),
                            cusAddress.getText(),
                            cusNIC.getText(),
                            cusDOB.getValue().toString()
                    )
            );

            if(status == 0){
                new Alert(Alert.AlertType.CONFIRMATION, "Success...!").show();
                ClearOnAction(actionEvent);
            }else if(status == 1){
                new Alert(Alert.AlertType.ERROR, "Duplicated Entry...!").show();
            } else if(status == 2) {
                new Alert(Alert.AlertType.ERROR, "Failed...!").show();
            }
            getAllCustomers();
        }catch(ConstraintViolationException e){
            new Alert(Alert.AlertType.ERROR, "Duplicated Entry...!").show();
        }
    }

    public void EditOnAction(ActionEvent actionEvent) throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to edit?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            customerService.Edit(
                    new CustomerDTO(
                            cusName.getText(),
                            cusPhone1.getText(),
                            cusPhone2.getText(),
                            cusAddress.getText(),
                            cusNIC.getText(),
                            cusDOB.getValue().toString()
                    ), currentSelectedId
            );
        }
        ClearOnAction(actionEvent);
        getAllCustomers();
    }

    public void DeleteOnAction(ActionEvent actionEvent) throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            customerService.Delete(currentSelectedId);
            ClearOnAction(actionEvent);
        }
        getAllCustomers();
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
        colCusPhone1.setCellValueFactory(new PropertyValueFactory<>("cusPhoneOne"));
        colCusPhone2.setCellValueFactory(new PropertyValueFactory<>("cusPhoneTwo"));
        colCusAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        colCusNIC.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colCusDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
    }

    @FXML
    public void tableOnMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Customer customer = tblCustomer.getSelectionModel().getSelectedItem();
            cusName.setText(customer.getCusName());
            cusPhone1.setText(customer.getCusPhoneOne());
            cusPhone2.setText(customer.getCusPhoneTwo());
            cusAddress.setText(customer.getCusAddress());
            cusNIC.setText(customer.getNic());
            cusDOB.setValue(LocalDate.parse(customer.getDob()));
            currentSelectedId = customer.getCusId();
        }
    }

    public void ClearOnAction(ActionEvent actionEvent) {
        cusName.clear();
        cusPhone1.clear();
        cusPhone2.clear();
        cusAddress.clear();
        cusNIC.clear();
        cusDOB.setValue(null);
    }
}
