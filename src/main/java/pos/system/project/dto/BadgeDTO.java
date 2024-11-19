package pos.system.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BadgeDTO {
    private String description;
    private double purchasePrice;
    private double sellingPrice;
    private double purchasePriceLiter;
    private double sellingPriceLiter;
    private BigDecimal quantity;
    private String expireDate;
    private int quantityType;
}
