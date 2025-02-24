package pos.system.project.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pos.system.project.entity.*;

import java.io.IOException;
import java.util.Properties;

public class FactoryConfiguration {
    private static FactoryConfiguration factoryConfiguration;
    private final SessionFactory sessionFactory;
    private FactoryConfiguration() throws IOException {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.properties"));
        configuration.setProperties(properties);
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Item.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(OrderDetail.class);
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Badge.class);
        configuration.addAnnotatedClass(ShortCut.class);
        configuration.addAnnotatedClass(CustomerLoan.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static FactoryConfiguration getInstance() throws IOException {
        return (factoryConfiguration==null) ? factoryConfiguration=new FactoryConfiguration():factoryConfiguration;
    }

    public Session getSession(){
        return sessionFactory.openSession();
    }
}
