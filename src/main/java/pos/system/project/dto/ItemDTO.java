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
public class ItemDTO {
    private String itemName;
    private String barcode;
    private int sellByStatus;
    private String categoryName;
    private String imageUrl;
}
