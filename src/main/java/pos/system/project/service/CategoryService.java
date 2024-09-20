package pos.system.project.service;

import pos.system.project.dto.CategoryDTO;
import pos.system.project.entity.Category;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface CategoryService {
    void addCategory(CategoryDTO categoryDTO) throws IOException;
    void editCategory(CategoryDTO categoryDTO, int currentSelectedId) throws IOException;
    void deleteCategory(int categoryId) throws IOException;
    List<Category> getAllCategories() throws IOException;
}
