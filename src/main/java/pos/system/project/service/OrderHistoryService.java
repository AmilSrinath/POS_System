package pos.system.project.service;

import pos.system.project.entity.tm.OrderHistoryTM;

import java.util.List;

/**
 * @author Amil Srinath
 */
public interface OrderHistoryService {
    List<OrderHistoryTM> getAllOrderHistory();
}
