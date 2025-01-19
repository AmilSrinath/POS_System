package pos.system.project.entity.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectOrderDetail {
    private int orderDetailID;
    private int itemID;
    private double itemPrice;
    private double quantity;
    private double subTotal;
    private Date date;
}
