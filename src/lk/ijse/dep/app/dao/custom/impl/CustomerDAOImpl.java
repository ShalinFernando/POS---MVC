package lk.ijse.dep.app.dao.custom.impl;

import lk.ijse.dep.app.dao.CrudUtil;
import lk.ijse.dep.app.dao.custom.CustomerDAO;
import lk.ijse.dep.app.entity.Customer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public Optional<Customer> find(String customerId) throws Exception {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Customer WHERE id=?", customerId);
        Customer c = null;
        if (rst.next()) {
            c = new Customer(rst.getString("id"),
                    rst.getString("name"),
                    rst.getString("address"));
        }
        return Optional.ofNullable(c);
    }

    public Optional<List<Customer>> findAll() throws Exception {
        ArrayList<Customer> alCustomerS = new ArrayList<>();
        ResultSet rst = CrudUtil.<ResultSet>execute("SELECT * FROM Customer");
        while (rst.next()) {
            String id = rst.getString(1);
            String name = rst.getString(2);
            String address = rst.getString(3);
            Customer customer = new Customer(id, name, address);
            alCustomerS.add(customer);
        }
        return Optional.ofNullable(alCustomerS);
    }

    public boolean save(Customer customer) throws Exception {
        return CrudUtil.<Integer>execute("INSERT INTO Customer VALUES (?,?,?)",
                customer.getId(), customer.getName(), customer.getAddress()) > 0;
    }

    public boolean update(Customer customer) throws Exception {
        return CrudUtil.<Integer>execute("UPDATE Customer SET name=?,address=? WHERE id=?",
                customer.getName(), customer.getAddress(), customer.getId()) > 0;
    }

    public boolean delete(String customerId) throws Exception {
        return CrudUtil.<Integer>execute("DELETE FROM Customer WHERE id=?", customerId) > 0;
    }

}
