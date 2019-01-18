package lk.ijse.dep.app.dao.custom.impl;

import lk.ijse.dep.app.dao.CrudUtil;
import lk.ijse.dep.app.dao.custom.OrderDAO;
import lk.ijse.dep.app.entity.Order;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public Optional<Order> find(String key) throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM `Order` WHERE id=?", key);
        Order order = null;
        if (rst.next()) {
            order = new Order(rst.getString("id"),
                    rst.getDate("date"),
                    rst.getString("customerId"));
        }
        return Optional.ofNullable(order);
    }

    @Override
    public Optional<List<Order>> findAll() throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM `Order`");
        List<Order> alOrders = new ArrayList<>();
        while (rst.next()) {
            Order order = new Order(rst.getString("id"),
                    rst.getDate("date"),
                    rst.getString("customerId"));
            alOrders.add(order);
        }
        return Optional.ofNullable(alOrders);
    }

    @Override
    public boolean save(Order entity) throws Exception {
        return CrudUtil.<Integer>execute("INSERT INTO `Order` VALUES (?,?,?)",
                entity.getId(), entity.getDate(), entity.getCustomerId()) > 0;
    }

    @Override
    public boolean update(Order entity) throws Exception {
        return CrudUtil.<Integer>execute("UPDATE `Order` SET date=?, customerId=? WHERE id=?",
                entity.getDate(), entity.getCustomerId(), entity.getId()) > 0;
    }

    @Override
    public boolean delete(String key) throws Exception {
        return CrudUtil.<Integer>execute("DELETE FROM `Order` WHERE id=?", key) > 0;
    }

    @Override
    public int count() throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT COUNT(*) FROM `Order`");
        if (rst.next()) {
            return rst.getInt(1);
        }
        return 0;
    }
}
