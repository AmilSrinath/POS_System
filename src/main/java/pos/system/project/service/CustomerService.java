package pos.system.project.service;

import pos.system.project.dto.CustomerDTO;
import pos.system.project.entity.Customer;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface CustomerService {
    Customer Add(CustomerDTO customerDTO) throws IOException;
    void Edit(CustomerDTO customerDTO, int currentSelectedId) throws IOException;
    void Delete(int currentSelectedId) throws IOException;
    List<Customer> getAllCustomers() throws IOException;
    Customer getCustomerByMobileNumber(String mobileNumber) throws IOException;
}
