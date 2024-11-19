package pos.system.project.service.impl;

import com.mysql.cj.jdbc.exceptions.PacketTooBigException;
import javafx.scene.control.Alert;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import pos.system.project.controller.HomeController;
import pos.system.project.controller.ItemController;
import pos.system.project.dto.BadgeDTO;
import pos.system.project.dto.ItemDTO;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Category;
import pos.system.project.entity.Item;
import pos.system.project.entity.tm.ItemTM;
import pos.system.project.service.ItemService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author Amil Srinath
 */
public class ItemServiceImpl implements ItemService {
    @Override
    public void addItem(ItemDTO itemDTO, BadgeDTO badgeDTO) throws IOException {
        Category category = null;

        // Find category based on itemDTO.getCategoryName()
        for (Category c : ItemController.categoryList) {
            if (c.getName().equals(itemDTO.getCategoryName()) && c.getStatus() == 1) {
                category = c;
                break;
            }
        }

        if (category == null) {
            throw new IOException("Category not found for name: " + itemDTO.getCategoryName());
        }

        // Start session and transaction
        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // Create and persist the new Item
            Item item = new Item(
                    0,
                    itemDTO.getBarcode(),
                    itemDTO.getItemName(),
                    itemDTO.getSellByStatus(),
                    1,
                    itemDTO.getImageUrl(),
                    category,
                    HomeController.user
            );

            session.persist(item);

            // Create and persist the associated Badge
            Badge badge = new Badge(
                    0,  // Assuming ID is auto-generated
                    badgeDTO.getDescription(),
                    badgeDTO.getPurchasePrice(),
                    badgeDTO.getSellingPrice(),
                    badgeDTO.getPurchasePriceLiter(),
                    badgeDTO.getSellingPriceLiter(),
                    badgeDTO.getQuantity(),
                    badgeDTO.getExpireDate(),
                    1,  // Status = Active
                    badgeDTO.getQuantityType(),
                    item  // Associate with the newly created Item
            );

            session.persist(badge);

            // Commit transaction
            transaction.commit();
        } catch (HibernateException e) {
            // Check if the cause is PacketTooBigException
            Throwable cause = e.getCause();
            if (cause instanceof com.mysql.cj.jdbc.exceptions.PacketTooBigException) {
                // Show alert for PacketTooBigException
                showAlert("Error", "Please check your image size and try again.");
            } else {
                throw new IOException("Error while saving the item and badge", e);
            }

            if (transaction != null) {
                transaction.rollback();  // Rollback in case of error
            }
        } finally {
            if (session != null) {
                session.close();  // Ensure session is closed
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void updateItem(ItemDTO itemDTO, int currentItemSelectedId, int currentBadgeSelectedId, BadgeDTO badgeDTO) throws IOException {
        // Obtain the session
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Retrieve the existing Item by ID
            Item existingItem = session.get(Item.class, currentItemSelectedId);
            if (existingItem != null) {
                // Update the Item entity with data from ItemDTO
                existingItem.setItemName(itemDTO.getItemName());
                existingItem.setItemBarcode(itemDTO.getBarcode());
                existingItem.setSellByStatus(itemDTO.getSellByStatus());
                existingItem.setImageUrl(itemDTO.getImageUrl());

                // Update the Item in the database
                session.update(existingItem);
            }

            // Retrieve the existing Badge by ID
            Badge existingBadge = session.get(Badge.class, currentBadgeSelectedId);
            if (existingBadge != null) {
                // Update the Badge entity with data from BadgeDTO
                existingBadge.setDescription(badgeDTO.getDescription());
                existingBadge.setPurchasePrice(badgeDTO.getPurchasePrice());
                existingBadge.setSellingPrice(badgeDTO.getSellingPrice());
                existingBadge.setPurchasePriceLiter(badgeDTO.getPurchasePriceLiter());
                existingBadge.setSellingPriceLiter(badgeDTO.getSellingPriceLiter());
                existingBadge.setQuantity(badgeDTO.getQuantity());
                existingBadge.setExpireDate(badgeDTO.getExpireDate());

                // Update the Badge in the database
                session.update(existingBadge);
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new IOException("Failed to update Item or Badge", e);
        } finally {
            session.close();
        }
    }


    @Override
    public void deleteItem(int itemId) throws IOException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // Use parameterized query to prevent SQL injection
            Query query = session.createNativeQuery("UPDATE item SET status = 0 WHERE itemId = :itemId");
            query.setParameter("itemId", itemId);
            query.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback in case of failure
            }
            throw new IOException("Failed to delete item", e);
        } finally {
            if (session != null) {
                session.close(); // Ensure session is closed
            }
        }
    }

    @Override
    public List<Item> getAllItems() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM item");
        nativeQuery.addEntity(Item.class);
        List<Item> itemList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return itemList;
    }

    @Override
    public List<ItemTM> getAll() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        List<ItemTM> itemListTM = null;

        try {
            transaction = session.beginTransaction();

            // Use a native query or HQL query to fetch data from Item and Badge tables
            String hql = "SELECT i, b FROM Item i LEFT JOIN Badge b ON i.itemId = b.item.itemId";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            List<Object[]> results = query.getResultList();

            itemListTM = results.stream().map(result -> {
                Item item = (Item) result[0];
                Badge badge = (Badge) result[1];

                // Mapping data to ItemTM object
                return new ItemTM(
                        item.getItemId(),
                        item.getItemBarcode(),
                        item.getItemName(),
                        item.getSellByStatus(),
                        item.getImageUrl(),
                        item.getCategory(),
                        badge != null ? badge.getBadgeId() : 0,
                        badge != null ? badge.getDescription() : null,
                        badge != null ? badge.getPurchasePrice() : 0,
                        badge != null ? badge.getSellingPrice() : 0,
                        badge != null ? badge.getQuantity() : BigDecimal.ZERO,
                        badge != null ? badge.getExpireDate() : null,
                        badge != null ? badge.getQuantityType() : 0
                );
            }).toList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException("Failed to fetch data for items and badges", e);
        } finally {
            session.close();
        }

        return itemListTM;
    }

}
