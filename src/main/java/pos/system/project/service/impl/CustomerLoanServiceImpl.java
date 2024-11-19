package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pos.system.project.entity.CustomerLoan;
import pos.system.project.service.CustomerLoanService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public class CustomerLoanServiceImpl implements CustomerLoanService {

    @Override
    public void saveLoan(CustomerLoan customerLoan) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(customerLoan);
        transaction.commit();
        session.close();
    }
}
