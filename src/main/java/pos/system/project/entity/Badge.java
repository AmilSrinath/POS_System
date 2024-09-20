package pos.system.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double purchasePrice;
    private double sellingPrice;
    private double quantity;
    private String expireDate;
    private int status;

    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;

    public Item getItem() {
        return item;
    }
}
