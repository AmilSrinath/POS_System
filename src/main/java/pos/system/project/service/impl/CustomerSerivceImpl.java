package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.controller.HomeController;
import pos.system.project.dto.CustomerDTO;
import pos.system.project.entity.Customer;
import pos.system.project.service.CustomerService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class CustomerSerivceImpl implements CustomerService {
    @Override
    public Customer Add(CustomerDTO customerDTO) throws IOException {
        // Create a new Customer object without manually setting the ID
        Customer customer = new Customer(
                0,  // Set 0 or leave blank, as the ID will be auto-generated
                customerDTO.getCusName(),
                customerDTO.getCusAddress(),
                customerDTO.getCusNIC(),
                customerDTO.getCusPhone(),
                customerDTO.getCusDOB(),
                1,
                HomeController.user
        );

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String query = "FROM Customer WHERE cusPhone = :phoneOne OR nic = :nic";
            Customer existingCustomer = session.createQuery(query, Customer.class)
                    .setParameter("phoneOne", customer.getCusPhone())
                    .setParameter("nic", customer.getNic())
                    .uniqueResult();

            if (existingCustomer == null) {
                session.persist(customer);
                transaction.commit();
                return customer; // Return the newly created customer
            } else {
                return null; // Duplicate phone number or NIC found
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null; // Return null if something went wrong
        } finally {
            session.close();
        }
    }

    @Override
    public void Edit(CustomerDTO customerDTO, int currentSelectedId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Customer customer = session.get(Customer.class, currentSelectedId);
        customer.setCusName(customerDTO.getCusName());
        customer.setCusAddress(customerDTO.getCusAddress());
        customer.setNic(customerDTO.getCusNIC());
        customer.setCusPhone(customerDTO.getCusPhone());
        customer.setDob(customerDTO.getCusDOB());
        session.saveOrUpdate(customer);
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

    @Override
    public Customer getCustomerByMobileNumber(String mobileNumber) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery<Customer> nativeQuery = session.createNativeQuery("SELECT * FROM customer WHERE cusPhone = :phone", Customer.class);
        nativeQuery.setParameter("phone", mobileNumber);

        List<Customer> customers = nativeQuery.getResultList();
        transaction.commit();
        session.close();

        // Return the first customer found or null if no customers found
        return customers.isEmpty() ? null : customers.get(0);
    }
}
