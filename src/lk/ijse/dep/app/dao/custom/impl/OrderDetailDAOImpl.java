package lk.ijse.dep.app.dao.custom.impl;

import lk.ijse.dep.app.dao.CrudUtil;
import lk.ijse.dep.app.dao.custom.OrderDetailDAO;
import lk.ijse.dep.app.entity.OrderDetail;
import lk.ijse.dep.app.entity.OrderDetailPK;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public Optional<OrderDetail> find(OrderDetailPK key) throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM OrderDetail WHERE orderId=? AND itemCode=?",
                key.getOrderId(), key.getItemCode());
        OrderDetail od = null;
        if (rst.next()) {
            String orderId = rst.getString("orderId");
            String itemCode = rst.getString("itemCode");
            int qty = rst.getInt("qty");
            double unitPrice = rst.getDouble("unitPrice");
            od = new OrderDetail(orderId, itemCode, qty, unitPrice);
        }
        return Optional.ofNullable(od);
    }

    @Override
    public Optional<List<OrderDetail>> findAll() throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM OrderDetail");
        List<OrderDetail> alOrderDetails = new ArrayList<>();

        if (rst.next()) {
            String orderId = rst.getString("orderId");
            String itemCode = rst.getString("itemCode");
            int qty = rst.getInt("qty");
            double unitPrice = rst.getDouble("unitPrice");
            OrderDetail orderDetail = new OrderDetail(orderId, itemCode, qty, unitPrice);
            alOrderDetails.add(orderDetail);
        }

        return Optional.ofNullable(alOrderDetails);
    }

    @Override
    public boolean save(OrderDetail entity) throws Exception {
        return CrudUtil.<Integer>execute("INSERT INTO OrderDetail VALUES (?,?,?,?)",
                entity.getOrderDetailPK().getOrderId(), entity.getOrderDetailPK().getItemCode(), entity.getQty(), entity.getUnitPrice()) > 0;
    }

    @Override
    public boolean update(OrderDetail entity) throws Exception {
        return CrudUtil.<Integer>execute("UPDATE OrderDetail SET qty=?, unitPrice=? WHERE orderId=? AND itemCode=?",
                entity.getQty(), entity.getUnitPrice(), entity.getOrderDetailPK().getOrderId(), entity.getOrderDetailPK().getItemCode()) > 0;
    }

    @Override
    public boolean delete(OrderDetailPK key) throws Exception {
        return CrudUtil.<Integer>execute("DELETE FROM OrderDetail WHERE orderId=? AND itemCode=?",
                key.getOrderId(), key.getItemCode()) > 0;
    }

    @Override
    public Optional<List<OrderDetail>> find(String orderId) throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM OrderDetail WHERE orderId=?", orderId);
        List<OrderDetail> alOrderDetails = new ArrayList<>();
        if (rst.next()) {
            String itemCode = rst.getString("itemCode");
            int qty = rst.getInt("qty");
            double unitPrice = rst.getDouble("unitPrice");
            alOrderDetails.add(new OrderDetail(orderId, itemCode, qty, unitPrice));
        }
        return Optional.ofNullable(alOrderDetails);
    }
}
