package lk.ijse.dep.app.dao.custom;

import lk.ijse.dep.app.dao.CrudDAO;
import lk.ijse.dep.app.entity.OrderDetail;
import lk.ijse.dep.app.entity.OrderDetailPK;

import java.util.List;
import java.util.Optional;

public interface OrderDetailDAO extends CrudDAO<OrderDetail, OrderDetailPK> {

    Optional<List<OrderDetail>> find(String orderId) throws Exception;

}
