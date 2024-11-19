package pos.system.project.service;

import pos.system.project.entity.CustomerLoan;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public interface CustomerLoanService {
    void saveLoan(CustomerLoan customerLoan) throws IOException;
}
