package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.controller.HomeController;
import pos.system.project.dto.CategoryDTO;
import pos.system.project.entity.Category;
import pos.system.project.entity.Customer;
import pos.system.project.service.CategoryService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class CategoryServiceImpl implements CategoryService {
    @Override
    public void addCategory(CategoryDTO categoryDTO) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        Category category = new Category(
                0,  // Set 0 or leave blank, as the ID will be auto-generated
                categoryDTO.getName(),
                1,
                HomeController.user
        );
        session.save(category);
        transaction.commit();
        session.close();
    }

    @Override
    public void editCategory(CategoryDTO categoryDTO, int currentSelectedId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Category category = session.get(Category.class, currentSelectedId);
        category.setName(categoryDTO.getName());
        session.update(category);
        transaction.commit();
        session.close();
    }

    @Override
    public void deleteCategory(int categoryId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery("UPDATE Category SET status = 0 WHERE categoryId = "+categoryId).executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public List<Category> getAllCategories() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM category");
        nativeQuery.addEntity(Category.class);
        List<Category> categoryList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return categoryList;
    }
}
