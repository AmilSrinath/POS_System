package pos.system.project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;
import pos.system.project.dto.CustomerDTO;
import pos.system.project.entity.Customer;
import pos.system.project.entity.CustomerLoan;
import pos.system.project.service.CustomerLoanService;
import pos.system.project.service.CustomerService;
import pos.system.project.service.impl.CustomerLoanServiceImpl;
import pos.system.project.service.impl.CustomerSerivceImpl;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Amil Srinath
 */
public class MobileNumberEnterController {
    @FXML
    public TextField txtMobileNumber;
    public AnchorPane moblieNumberEnterForm;
    CustomerService customerService = new CustomerSerivceImpl();
    CustomerLoanService customerLoanService = new CustomerLoanServiceImpl();

    @FXML
    public Label lblCustomerName;

    private boolean enterPressedOnce = false;
    private Timer enterTimer = new Timer();

    private double totalAmount = 0.0;

    @FXML
    public void initialize() {
        txtMobileNumber.setOnKeyPressed(this::handleKeyPress);
    }

    private void handleKeyPress(KeyEvent event)  {
        if (event.getCode() == KeyCode.ENTER) {
            if (!enterPressedOnce) {
                enterPressedOnce = true;
                // Set a delay (e.g., 300 milliseconds) to distinguish between single and double Enter presses
                enterTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (enterPressedOnce) {
                            // Single Enter key press logic
                            singleEnterPressAction();
                        }
                        enterPressedOnce = false;
                    }
                }, 300);
            } else {
                // Double Enter key press logic
                enterPressedOnce = false; // Reset state
                try {
                    doubleEnterPressAction();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void singleEnterPressAction() {
        try {
            System.out.println(lblCustomerName.getText());
            Customer customer = customerService.getCustomerByMobileNumber(txtMobileNumber.getText());
            System.out.println(customer);

            // Use Platform.runLater to update the UI safely
            javafx.application.Platform.runLater(() -> {
                lblCustomerName.setText(customer != null ? customer.getCusName() : "No customer found");
                lblCustomerName.setStyle("-fx-text-fill: red");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doubleEnterPressAction() throws IOException {
        //Todo add or update customer
        Customer customer = customerService.getCustomerByMobileNumber(txtMobileNumber.getText());
        CustomerLoan customerLoan = new CustomerLoan();
        customerLoan.setLoanAmount(totalAmount);
        customerLoan.setStatus(1);

        if (customer != null) {
            //Todo add customerLoan row
            customerLoan.setCustomer(customer);
            customerLoan.setCusMobileNumber(txtMobileNumber.getText());
            customerLoan.setCusName(customer.getCusName());

            customerLoanService.saveLoan(customerLoan);
            //hide dialog
            moblieNumberEnterForm.getScene().getWindow().hide();

        }else {
            //Todo add customer and customerLoan
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCusPhone(txtMobileNumber.getText());

            customerService.Add(customerDTO);
            customerLoan.setCustomer(customerService.getCustomerByMobileNumber(txtMobileNumber.getText()));
            customerLoanService.saveLoan(customerLoan);
            //hide dialog
            moblieNumberEnterForm.getScene().getWindow().hide();
        }
    }

    public void setTotalAmount(String text) {
        this.totalAmount = Double.parseDouble(text);
    }
}