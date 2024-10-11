package pos.system.project.service;

import pos.system.project.dto.OrderDTO;
import pos.system.project.dto.OrderDetailsDTO;
import pos.system.project.entity.Order;

import java.io.IOException;

/**
 * @author Amil Srinath
 */
public interface OrderService {
    Order saveOrder(OrderDTO orderDTO) throws IOException;
    void saveOrderDetails(OrderDetailsDTO orderDetailsDTO) throws IOException;
    void updateQuantity(int badgeId, double quantity, double milliliter, int status) throws IOException;
}
