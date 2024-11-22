package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import pos.system.project.entity.Item;
import pos.system.project.entity.OrderDetail;
import pos.system.project.service.OrderDetailService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class OrderDetailServiceImpl implements OrderDetailService {

    @Override
    public List<OrderDetail> getOrderHistoryByOrderId(int orderId) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("SELECT * FROM order_detail WHERE orderId = :orderId").setParameter("orderId", orderId);
        nativeQuery.addEntity(OrderDetail.class);
        List<OrderDetail> orderDetailList = nativeQuery.getResultList();
        transaction.commit();
        session.close();
        return orderDetailList;
    }
}
