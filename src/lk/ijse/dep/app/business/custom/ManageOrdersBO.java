package lk.ijse.dep.app.business.custom;

import lk.ijse.dep.app.business.SuperBO;
import lk.ijse.dep.app.dto.OrderDTO;
import lk.ijse.dep.app.dto.OrderDTO2;

import java.util.List;

public interface ManageOrdersBO extends SuperBO {

    List<OrderDTO2> getOrdersWithCustomerNamesAndTotals() throws Exception;

    List<OrderDTO> getOrders() throws Exception;

    String generateOrderId() throws Exception;

    void createOrder(OrderDTO dto) throws Exception;

    OrderDTO findOrder(String orderId) throws Exception;

}
