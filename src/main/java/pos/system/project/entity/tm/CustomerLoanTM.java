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
public class CustomerLoanTM {
    private String mobileNumber;
    private String name;
    private double totalAmount;
}
