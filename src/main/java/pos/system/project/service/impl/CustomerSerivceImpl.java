package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.controller.HomeController;
import pos.system.project.dto.CustomerDTO;
import pos.system.project.entity.Customer;
import pos.system.project.entity.User;
import pos.system.project.service.CustomerService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class CustomerSerivceImpl implements CustomerService {
    @Override
    public int Add(CustomerDTO customerDTO) throws IOException {
        // Create a new Customer object without manually setting the ID
        Customer customer = new Customer(
                0,  // Set 0 or leave blank, as the ID will be auto-generated
                customerDTO.getCusName(),
                customerDTO.getCusAddress(),
                customerDTO.getCusNIC(),
                customerDTO.getCusPhone1(),
                customerDTO.getCusPhone2(),
                customerDTO.getCusDOB(),
                1,
                HomeController.user
        );

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String query = "FROM Customer WHERE cusPhoneOne = :phoneOne OR cusPhoneTwo = :phoneTwo OR nic = :nic";
            Customer existingCustomer = session.createQuery(query, Customer.class)
                    .setParameter("phoneOne", customer.getCusPhoneOne())
                    .setParameter("phoneTwo", customer.getCusPhoneTwo())
                    .setParameter("nic", customer.getNic())
                    .uniqueResult();

            if (existingCustomer == null) {
                session.persist(customer);
            } else {
                return 1;// Duplicate phone number found
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return 2;// Something went wrong
        } finally {
            session.close();
        }
        return 0;// Success
    }

    @Override
    public void Edit(CustomerDTO customerDTO, int currentSelectedId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, currentSelectedId);
        customer.setCusName(customerDTO.getCusName());
        customer.setCusAddress(customerDTO.getCusAddress());
        customer.setNic(customerDTO.getCusNIC());
        customer.setCusPhoneOne(customerDTO.getCusPhone1());
        customer.setCusPhoneTwo(customerDTO.getCusPhone2());
        customer.setDob(customerDTO.getCusDOB());
        session.update(customer);
        transaction.commit();
        session.close();
    }

    @Override
    public void Delete(int currentSelectedId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery("UPDATE customer SET status = 0 WHERE cusId = " + currentSelectedId).executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public List<Customer> getAllCustomers() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM customer");
        nativeQuery.addEntity(Customer.class);
        List<Customer> itemList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return itemList;
    }
}
