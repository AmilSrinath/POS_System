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
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cusId;
    private String cusName;
    private String cusAddress;
    private String nic;
    @Column(unique = true)
    private String cusPhoneOne;
    @Column(unique = true)
    private String cusPhoneTwo;
    private String dob;
    private int status; //0 1
}
