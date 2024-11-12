package pos.system.project.entity.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderHistoryTM {
    private String orderID;
    private String customerID;
    private String customerName;
    private String date;
    private String total;
    private String userID;
}
