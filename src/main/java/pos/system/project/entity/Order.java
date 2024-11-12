package pos.system.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private Date createDate;
    private double total;
    private double amountPaid;
    private double balance;

    @ManyToOne
    @JoinColumn(name = "cusId")
    private Customer customer;
    private String customerName;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    // One order can have many order details
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
