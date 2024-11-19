package pos.system.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pos.system.project.entity.Item;
import pos.system.project.entity.Order;
import pos.system.project.entity.User;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailsDTO {
    private double quantity;
    private double subTotal;
    private double itemPrice;
    private int badgeId;
    private String itemType;
    private Order order;
    private Item item;
    private User user;
}
