package pos.system.project.entity.tm;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Amil Srinath
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTM {
    private String itemBarcode;
    private String itemName;
    private double quantity;
    private String type;
    private double unitPrice;
    private double subTotal;
    private int badgeId;
    private double milliliter;
    private int status; // 1-qb 2-hb 3-b 4-litters
}
