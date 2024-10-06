package pos.system.project.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Customer customer;
    private User user;
}
