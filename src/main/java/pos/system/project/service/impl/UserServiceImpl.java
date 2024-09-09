package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.dto.UserDTO;
import pos.system.project.entity.Customer;
import pos.system.project.entity.User;
import pos.system.project.service.UserService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class UserServiceImpl implements UserService {
    @Override
    public int Add(UserDTO userDTO) throws IOException {
        User customer = new User(
                0,  // Set 0 or leave blank, as the ID will be auto-generated
                userDTO.getUserName(),
                userDTO.getUserPassword(),
                userDTO.getUserEmail(),
                userDTO.getUserRole(),
                1
        );

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String query = "FROM User WHERE username = :username OR email = :email";
            Customer existingCustomer = session.createQuery(query, Customer.class)
                    .setParameter("username", customer.getUsername())
                    .setParameter("email", customer.getEmail())
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
    public void Edit(UserDTO userDTO, int currentSelectedId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, currentSelectedId);
        user.setUsername(userDTO.getUserName());
        user.setPassword(userDTO.getUserPassword());
        user.setEmail(userDTO.getUserEmail());
        user.setRole(userDTO.getUserRole());
        session.update(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void Delete(int currentSelectedId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.createNativeQuery("UPDATE user SET status = 0 WHERE userId = " + currentSelectedId).executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUser() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM user");
        nativeQuery.addEntity(User.class);
        List<User> userList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return userList;
    }
}
