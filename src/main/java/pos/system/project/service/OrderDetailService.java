package pos.system.project.service;

import pos.system.project.entity.OrderDetail;

import java.io.IOException;
import java.util.List;

/**
 * @author Amil Srinath
 */
public interface OrderDetailService {
    List<OrderDetail> getOrderHistoryByOrderId(int orderId) throws IOException;
}
