package pos.system.project.controller;

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
import pos.system.project.dto.CategoryDTO;
import pos.system.project.entity.Category;
import pos.system.project.service.CategoryService;
import pos.system.project.service.impl.CategoryServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Amil Srinath
 */
public class CategoryController {
    CategoryService categoryService = new CategoryServiceImpl();
    int currentSelectedId = -1;
    public static List<Category> categoryList;
    ObservableList<Category> observableList;

    @FXML
    public Button btnBack;
    @FXML
    public Label lblUsername;
    @FXML
    public TextField txtcategoryName;
    @FXML
    public TableView<Category> tblCategory;
    @FXML
    public TableColumn<?,?> colCategoryName;

    public void initialize() throws IOException {
        lblUsername.setText(HomeController.user.getUsername());
        getAllCategories();
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
    public void AddOnAction(ActionEvent actionEvent) throws IOException {
        categoryService.addCategory(
                new CategoryDTO(
                        txtcategoryName.getText(),
                        1
                ));

        txtcategoryName.clear();
        getAllCategories();
    }

    @FXML
    public void EditOnAction(ActionEvent actionEvent) throws IOException {
        if (currentSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select a category...!").show();
            return;
        }

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to edit?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            categoryService.editCategory(
                    new CategoryDTO(
                            txtcategoryName.getText(),
                            1
                    ), currentSelectedId
            );
            currentSelectedId = -1;
            txtcategoryName.clear();
            getAllCategories();
        }
    }

    private void getAllCategories() throws IOException {
        categoryList = categoryService.getAllCategories();
        observableList = FXCollections.observableArrayList();

        for (Category category : categoryList) {
            if (category.getStatus() == 1) {
                observableList.add(category);
            }
        }
        tblCategory.setItems(observableList);
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    @FXML
    public void DeleteOnAction(ActionEvent actionEvent) throws IOException {
        if (currentSelectedId == -1) {
            new Alert(Alert.AlertType.ERROR, "Please select a category...!").show();
            return;
        }

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            categoryService.deleteCategory(currentSelectedId);
            currentSelectedId = -1;
            txtcategoryName.clear();
            getAllCategories();
        }
    }

    @FXML
    public void ClearOnAction(ActionEvent actionEvent) {
        txtcategoryName.clear();
        currentSelectedId = -1;
    }

    @FXML
    public void tableOnMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Category category = tblCategory.getSelectionModel().getSelectedItem();
            txtcategoryName.setText(category.getName());
            currentSelectedId = category.getCategoryId();
        }
    }
}
