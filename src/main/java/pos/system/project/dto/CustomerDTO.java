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
public class CustomerDTO {
    private String cusName;
    private String cusPhone1;
    private String cusPhone2;
    private String cusAddress;
    private String cusNIC;
    private String cusDOB;
}
