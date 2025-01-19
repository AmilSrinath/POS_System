package pos.system.project.service;

import pos.system.project.entity.CustomerLoan;
import pos.system.project.entity.tm.CustomerLoanTM;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface CustomerLoanService {
    void saveLoan(CustomerLoan customerLoan) throws IOException;
    List<CustomerLoanTM> getAllLoans() throws IOException;
}
