package pos.system.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Amil Srinath
 */
@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cusId;
    private String cusName;
    private String cusAddress;
    @Column(unique = true)
    private String nic;
    @Column(unique = true)
    private String cusPhone;
    private LocalDate dob;
    private int status;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
