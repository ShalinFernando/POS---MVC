package lk.ijse.dep.app.dao.custom.impl;

import lk.ijse.dep.app.dao.CrudUtil;
import lk.ijse.dep.app.dao.custom.QueryDAO;
import lk.ijse.dep.app.entity.CustomEntity;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public Optional<List<CustomEntity>> findOrderDetailsWithItemDescriptions(String orderId) throws Exception {

        ResultSet rst = CrudUtil.execute("SELECT itemCode,qty,OrderDetail.unitPrice,description FROM OrderDetail\n" +
                "    INNER JOIN Item I on OrderDetail.itemCode = I.code WHERE orderId=?", orderId);
        List<CustomEntity> al = new ArrayList<>();

        while (rst.next()) {
            CustomEntity customEntity = new CustomEntity(rst.getString(1),
                    rst.getInt(2),
                    rst.getDouble(3),
                    rst.getString(4));
            al.add(customEntity);
        }
        return Optional.ofNullable(al);
    }

    @Override
    public Optional<List<CustomEntity>> findAllOrdersWithCustomerNameAndTotal() throws Exception {

        ResultSet rst = CrudUtil.execute("SELECT o.id, o.date, o.customerId, C.name,\n" +
                "       SUM(Detail.qty * Detail.unitPrice) AS Total FROM `Order` AS o\n" +
                " INNER JOIN Customer C on o.customerId = C.id\n" +
                "INNER JOIN OrderDetail Detail on o.id = Detail.orderId GROUP BY o.id");

        List<CustomEntity> al = new ArrayList<>();

        while (rst.next()) {
            CustomEntity customEntity = new CustomEntity(rst.getString(1),
                    rst.getDate(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5));
            al.add(customEntity);
        }
        return Optional.ofNullable(al);
    }
}
