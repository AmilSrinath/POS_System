package pos.system.project.service;

import pos.system.project.dto.OrderDTO;
import pos.system.project.dto.OrderDetailsDTO;
import pos.system.project.entity.Order;
import pos.system.project.entity.OrderDetail;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface OrderService {
    Order saveOrder(OrderDTO orderDTO) throws IOException;
    List<Order> getAllOrders() throws IOException;
    void saveOrderDetails(OrderDetailsDTO orderDetailsDTO) throws IOException;
    void updateQuantity(int badgeId, double quantity, double milliliter, int status) throws IOException;
    List<Order> getOrdersByDateRange(LocalDate from, LocalDate to) throws IOException;
}
