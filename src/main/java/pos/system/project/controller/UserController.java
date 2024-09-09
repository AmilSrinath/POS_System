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
import pos.system.project.dto.UserDTO;
import pos.system.project.entity.Customer;
import pos.system.project.entity.User;
import pos.system.project.service.UserService;
import pos.system.project.service.impl.UserServiceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Amil Srinath
 */
public class UserController {
    UserService userService = new UserServiceImpl();
    public static List<User> userList;
    ObservableList<User> observableList;
    int currentSelectedId = -1;

    @FXML
    public Button btnBack;
    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colRole;

    @FXML
    private TableColumn<?, ?> colUsername;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private TableView<User> tblUser;

    @FXML
    private TextField userEmail;

    @FXML
    private TextField userName;

    @FXML
    private TextField userPassword;

    @FXML
    private TextField userRePassword;

    public void initialize() throws IOException {
        TimeNow();
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        cmbRole.getItems().addAll( "SUPER ADMIN", "ADMIN", "USER");
        getAllUser();
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
    void AddOnAction(ActionEvent event) throws IOException {
        if (!userPassword.getText().equals(userRePassword.getText())) {
            new Alert(Alert.AlertType.ERROR, "Password does not match...!").show();
            return;
        }
        int status = userService.Add(
                new UserDTO(
                        userName.getText(),
                        userEmail.getText(),
                        userPassword.getText(),
                        cmbRole.getValue()
                )
        );

        if(status == 0){
            new Alert(Alert.AlertType.CONFIRMATION, "Success...!").show();
            ClearOnAction(event);
        } else if(status == 1){
            new Alert(Alert.AlertType.ERROR, "Duplicated Entry...!").show();
        } else if(status == 2){
            new Alert(Alert.AlertType.ERROR, "Failed...!").show();
        }
        getAllUser();
        currentSelectedId = -1;
    }

    @FXML
    void ClearOnAction(ActionEvent event) {
        userName.clear();
        userEmail.clear();
        userPassword.clear();
        userRePassword.clear();
        cmbRole.setValue(null);
    }

    @FXML
    void DeleteOnAction(ActionEvent event) throws IOException {
        if (currentSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select a user...!").show();
            return;
        }
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?", yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(no) == yes) {
            userService.Delete(currentSelectedId);
            ClearOnAction(event);
        }
        getAllUser();
        currentSelectedId = -1;
    }

    @FXML
    void EditOnAction(ActionEvent event) throws IOException {
        if (currentSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select a user...!").show();
            return;
        }

        if (!userPassword.getText().equals(userRePassword.getText())) {
            new Alert(Alert.AlertType.ERROR, "Password does not match...!").show();
            return;
        }

        userService.Edit(
                new UserDTO(
                        userName.getText(),
                        userEmail.getText(),
                        userPassword.getText(),
                        cmbRole.getValue()
                ), currentSelectedId
        );
        ClearOnAction(event);
        getAllUser();
        currentSelectedId = -1;
    }

    @FXML
    void tableOnMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            User user = tblUser.getSelectionModel().getSelectedItem();
            userName.setText(user.getUsername());
            userEmail.setText(user.getEmail());
            userPassword.setText(user.getPassword());
            userRePassword.setText(user.getPassword());
            cmbRole.setValue(user.getRole());
            currentSelectedId = user.getUserId();
        }
    }

    private void TimeNow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            while (!false) {
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

    public void getAllUser() throws IOException {
        userList = userService.getAllUser();
        observableList = FXCollections.observableArrayList();

        for (User user : userList) {
            if (user.getStatus() == 1) {
                observableList.add(user);
            }
        }
        tblUser.setItems(observableList);
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    }
}
