package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.entity.Item;
import pos.system.project.entity.tm.OrderHistoryTM;
import pos.system.project.service.OrderHistoryService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Override
    public List<OrderHistoryTM> getAllOrderHistory() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM order_detail");
        List<OrderHistoryTM> orderHistoryTMList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return orderHistoryTMList;
    }
}
