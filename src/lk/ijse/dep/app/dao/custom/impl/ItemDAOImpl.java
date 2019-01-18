package lk.ijse.dep.app.dao.custom.impl;

import lk.ijse.dep.app.dao.CrudUtil;
import lk.ijse.dep.app.dao.custom.ItemDAO;
import lk.ijse.dep.app.entity.Item;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAOImpl implements ItemDAO {

    public Optional<List<Item>> findAll() throws Exception {
        ArrayList<Item> alItemS = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM Item");
        while (rst.next()) {
            String code = rst.getString(1);
            String description = rst.getString(2);
            double unitPrice = rst.getDouble(3);
            int qty = rst.getInt(4);
            Item item = new Item(code, description, unitPrice, qty);
            alItemS.add(item);
        }
        return Optional.ofNullable(alItemS);
    }

    public boolean save(Item item) throws Exception {
        return CrudUtil.<Integer>execute("INSERT INTO Item VALUES (?,?,?,?)",
                item.getCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand()) > 0;
    }

    public boolean update(Item item) throws Exception {
        return CrudUtil.<Integer>execute("UPDATE Item SET description=?,unitPrice=?,qtyOnHand=? WHERE code=?",
                item.getDescription(), item.getUnitPrice(), item.getQtyOnHand(), item.getCode()) > 0;
    }

    public boolean delete(String code) throws Exception {
        return CrudUtil.<Integer>execute("DELETE FROM Item WHERE code=?", code) > 0;
    }

    @Override
    public Optional<Item> find(String itemCode) throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Item WHERE code=?", itemCode);
        Item item = null;
        if (rst.next()) {
            item = new Item(rst.getString("code"),
                    rst.getString("description"),
                    rst.getDouble("unitPrice"),
                    rst.getInt("qtyOnHand"));
        }
        return Optional.ofNullable(item);
    }
}
