package pos.system.project.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "badge")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int badgeId;
    private String description;
    private double purchasePrice;
    private double sellingPrice;
    private double purchasePriceLiter;
    private double sellingPriceLiter;
    private BigDecimal quantity;
    private String expireDate;
    private int status;
    private int quantityType;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    public Item getItem() {
        return item;
    }
}
