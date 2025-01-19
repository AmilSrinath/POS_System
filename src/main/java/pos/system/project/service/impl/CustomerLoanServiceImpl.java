package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pos.system.project.entity.CustomerLoan;
import pos.system.project.entity.tm.CustomerLoanTM;
import pos.system.project.service.CustomerLoanService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

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

    @Override
    public List<CustomerLoanTM> getAllLoans() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<CustomerLoanTM> loanSummaryList = null;

        try {
            // Execute the query and map the results to a list
            String hql = "SELECT new pos.system.project.entity.tm.CustomerLoanTM(cusMobileNumber, cusName, SUM(loanAmount)) " +
                    "FROM CustomerLoan " +
                    "GROUP BY cusMobileNumber, cusName";
            loanSummaryList = session.createQuery(hql, CustomerLoanTM.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return loanSummaryList;
    }
}
