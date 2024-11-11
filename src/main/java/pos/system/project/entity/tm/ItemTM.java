package pos.system.project.entity.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pos.system.project.entity.Category;

import java.math.BigDecimal;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemTM {
    private int itemId;
    private String itemBarcode;
    private String itemName;
    private int sellByStatus;
    private String imageUrl;
    private Category category;
    private int badgeId;
    private String description;
    private double purchasePrice;
    private double sellingPrice;
    private BigDecimal quantity;
    private String expireDate;
    private int quantityType;
}
