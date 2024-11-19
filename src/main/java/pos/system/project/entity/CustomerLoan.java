package pos.system.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "customerLoan")
public class CustomerLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int loanId;
    private double loanAmount;
    private Date date = new Date();
    private int status;
    private String cusMobileNumber;
    private String cusName;

    @ManyToOne
    @JoinColumn(name = "cusId")
    private Customer customer;
}
