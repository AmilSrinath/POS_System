package pos.system.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BadgeDTO {
    private double purchasePrice;
    private double sellingPrice;
    private double quantity;
    private String expireDate;
}
