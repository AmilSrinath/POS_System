package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.controller.HomeController;
import pos.system.project.controller.ItemController;
import pos.system.project.dto.BadgeDTO;
import pos.system.project.dto.ItemDTO;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Category;
import pos.system.project.entity.Item;
import pos.system.project.service.ItemService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
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
                    badgeDTO.getPurchasePrice(),
                    badgeDTO.getSellingPrice(),
                    badgeDTO.getQuantity(),
                    badgeDTO.getExpireDate(),
                    1,  // Status = Active
                    item  // Associate with the newly created Item
            );

            session.persist(badge);

            // Commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();  // Rollback in case of an error
            }
            throw new IOException("Error while saving the item and badge", e);
        } finally {
            if (session != null) {
                session.close();  // Ensure session is closed
            }
        }
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
                existingBadge.setPurchasePrice(badgeDTO.getPurchasePrice());
                existingBadge.setSellingPrice(badgeDTO.getSellingPrice());
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
}
