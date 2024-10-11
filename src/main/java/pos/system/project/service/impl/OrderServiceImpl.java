package pos.system.project.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import pos.system.project.dto.OrderDTO;
import pos.system.project.dto.OrderDetailsDTO;
import pos.system.project.entity.Badge;
import pos.system.project.entity.Order;
import pos.system.project.entity.OrderDetail;
import pos.system.project.service.OrderService;
import pos.system.project.util.FactoryConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Amil Srinath
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public Order saveOrder(OrderDTO orderDTO) throws IOException {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setTotal(orderDTO.getTotal());
        order.setCustomer(orderDTO.getCustomer());
        order.setUser(orderDTO.getUser());

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Object save = session.save(order);
        transaction.commit();
        session.close();

        return order;
    }

    @Override
    public void saveOrderDetails(OrderDetailsDTO orderDetailsDTO) throws IOException {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(orderDetailsDTO.getQuantity());
        orderDetail.setSubTotal(orderDetailsDTO.getSubTotal());
        orderDetail.setItemPrice(orderDetailsDTO.getItemPrice());
        orderDetail.setOrder(orderDetailsDTO.getOrder());
        orderDetail.setItem(orderDetailsDTO.getItem());
        orderDetail.setUser(orderDetailsDTO.getUser());
        orderDetail.setBadgeId(orderDetailsDTO.getBadgeId());

        Session session = null;
        Transaction transaction = null;

        try {
            session = FactoryConfiguration.getInstance().getSession();
            transaction = session.beginTransaction();

            session.save(orderDetail);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException("Failed to save order details", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void updateQuantity(int badgeId, double incomingQuantity, double milliliter, int status) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = null;

        System.out.println(milliliter);

        try {
            transaction = session.beginTransaction();
            Badge badge = session.get(Badge.class, badgeId);
            if (badge != null) {
                BigDecimal updatedQuantity = null;
                BigDecimal currentQuantity = badge.getQuantity();
                if (status == 0) {
                    updatedQuantity = currentQuantity.subtract(BigDecimal.valueOf(incomingQuantity));
                } else if (status == 1) {
                    updatedQuantity = currentQuantity.subtract(BigDecimal.valueOf(187.5));
                } else if (status == 2) {
                    updatedQuantity = currentQuantity.subtract(BigDecimal.valueOf(375));
                } else if (status == 3) {
                    updatedQuantity = currentQuantity.subtract(BigDecimal.valueOf(750));
                } else if (status == 4) {
                    updatedQuantity = currentQuantity.subtract(BigDecimal.valueOf(milliliter*1000));
                }

                badge.setQuantity(updatedQuantity);
                session.update(badge);
                transaction.commit();
                System.out.println("Badge quantity updated successfully. Updated Quantity: " + updatedQuantity);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new IOException("Error updating badge quantity", e);
        } finally {
            session.close();
        }
    }

}
