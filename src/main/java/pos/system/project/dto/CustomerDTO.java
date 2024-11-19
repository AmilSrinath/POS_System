package pos.system.project.dto;

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
public class CustomerDTO {
    private String cusName;
    private String cusPhone;
    private String cusAddress;
    private String cusNIC;
    private LocalDate cusDOB;
}
