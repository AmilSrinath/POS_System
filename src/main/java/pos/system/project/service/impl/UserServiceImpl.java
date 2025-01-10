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

    @Override
    public User getUserByUsername(String username) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();

            // Use HQL (Hibernate Query Language) to fetch the user by username
            String hql = "FROM User WHERE username = :username AND status = 1";
            user = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .uniqueResult();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public boolean checkCredentials(String username, String password) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean isValid = false;

        try {
            transaction = session.beginTransaction();

            // HQL query to check for the username and password
            String hql = "FROM User WHERE username = :username AND password = :password AND status = 1";
            User user = session.createQuery(hql, User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            if (user != null) {
                isValid = true; // User with valid credentials found
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return isValid;
    }

    @Override
    public boolean isUserExist(String text) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        boolean isValid = false;

        try {
            transaction = session.beginTransaction();

            // HQL query to check for the username and password
            String hql = "FROM User WHERE email = :email AND status = 1";
            User user = session.createQuery(hql, User.class)
                    .setParameter("email", text)
                    .uniqueResult();

            if (user != null) {
                isValid = true;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return isValid;
    }

    @Override
    public void resetPassword(String email, String password) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // Fetch user by email
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();

            if (user != null) {
                user.setPassword(password);
                session.update(user);
                transaction.commit();
            } else {
                throw new IllegalArgumentException("User not found with email: " + email);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public boolean isEmpty() throws IOException {
        Session session = null;
        Transaction transaction = null;
        try {
            // Open a session and begin a transaction
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            // Execute the query to count users
            Long count = (Long) session.createNativeQuery("SELECT count(userId) FROM user where status = 1").getSingleResult();

            // Commit the transaction
            transaction.commit();

            // Check if count is 0
            return count == 0;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback if something goes wrong
            }
            throw new IOException("Error checking if the user table is empty", e);
        } finally {
            if (session != null) {
                session.close(); // Ensure session is always closed
            }
        }
    }

}
