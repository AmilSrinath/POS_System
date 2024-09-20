package pos.system.project.service;

import pos.system.project.dto.BadgeDTO;
import pos.system.project.dto.ItemDTO;
import pos.system.project.entity.Item;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface ItemService {
    void addItem(ItemDTO itemDTO, BadgeDTO badgeDTO) throws IOException;
    void updateItem(ItemDTO itemDTO, int currentItemSelectedId, int currentBadgeSelectedId, BadgeDTO badgeDTO) throws IOException;
    void deleteItem(int itemId) throws IOException;
    List<Item> getAllItems() throws IOException;
}
