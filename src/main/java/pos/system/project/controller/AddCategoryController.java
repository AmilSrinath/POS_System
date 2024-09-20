package pos.system.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pos.system.project.dto.CategoryDTO;
import pos.system.project.service.CategoryService;
import pos.system.project.service.impl.CategoryServiceImpl;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class AddCategoryController {
    CategoryService categoryService = new CategoryServiceImpl();

    @FXML
    public TextField categoryName;

    @FXML
    public void AddOnAction(ActionEvent actionEvent) throws IOException {
        categoryService.addCategory(
                new CategoryDTO(
                        categoryName.getText(),
                        1
                ));
    }
}
