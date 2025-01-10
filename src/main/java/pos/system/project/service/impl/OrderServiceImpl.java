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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Amil Srinath
 */
public class OrderServiceImpl implements OrderService {

    @Override
    public Order saveOrder(OrderDTO orderDTO) throws IOException {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setAmountPaid(orderDTO.getAmountPaid());
        order.setBalance(orderDTO.getBalance());
        order.setTotal(orderDTO.getTotal());
        order.setIsPaid(orderDTO.getIsPaid());
        order.setCustomer(orderDTO.getCustomer());
        order.setUser(orderDTO.getUser());
        order.setCustomerName(orderDTO.getCustomerName());

        Session session = FactoryConfiguration.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        Object save = session.save(order);
        transaction.commit();
        session.close();

        return order;
    }

    @Override
    public List<Order> getAllOrders() throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        List<Order> orderList = session.createQuery("FROM Order").list();
        session.close();
        return orderList;
    }

    @Override
    public void saveOrderDetails(OrderDetailsDTO orderDetailsDTO) throws IOException {
        System.out.println("DONE : "+orderDetailsDTO.getOrder());

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setQuantity(orderDetailsDTO.getQuantity());
        orderDetail.setSubTotal(orderDetailsDTO.getSubTotal());
        orderDetail.setItemPrice(orderDetailsDTO.getItemPrice());
        orderDetail.setItemType(orderDetailsDTO.getItemType());
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

    @Override
    public List<Order> getOrdersByDateRange(LocalDate from, LocalDate to) throws IOException {
        Session session = FactoryConfiguration.getInstance().getSession();
        // Add one day to 'to' to include today's entries if they have time components
        List<Order> orderList = session.createQuery("FROM Order WHERE createDate BETWEEN :from AND :to")
                .setParameter("from", java.sql.Date.valueOf(from))
                .setParameter("to", java.sql.Date.valueOf(to.plusDays(1)))  // Include the entire day
                .list();
        session.close();
        return orderList;
    }
}
