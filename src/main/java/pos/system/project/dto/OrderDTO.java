package pos.system.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pos.system.project.entity.Customer;
import pos.system.project.entity.User;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private String createDate;
    private double total;
    private double amountPaid;
    private double balance;
    private int isPaid;
    private String customerName;
    private Customer customer;
    private User user;
}
