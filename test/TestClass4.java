import lk.ijse.dep.app.dao.DAOFactory;
import lk.ijse.dep.app.dao.SuperDAO;
import lk.ijse.dep.app.dao.custom.CustomerDAO;
import lk.ijse.dep.app.entity.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestClass4 {

    public static void main(String[] args) throws SQLException {

        System.out.println(Character.codePointAt("e",0));

        CustomerDAO dao = DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);
        Optional<List<Customer>> all = null;
        try {
            all = dao.findAll();
        } catch (Exception e) {
            Logger.getLogger("").log(Level.SEVERE, null, e);
        }

        List<Customer> galleCustomerList = all.get().stream().filter(new Predicate<Customer>() {
            @Override
            public boolean test(Customer customer) {
                return customer.getAddress().equals("Galle");
            }
        }).collect(Collectors.toList());

        for (Customer customer : galleCustomerList) {
            System.out.println(customer);
        }


//        String name = "Institute of Software Engineering";
//        name.chars().filter(new IntPredicate() {
//            @Override
//            public boolean test(int value) {
//                return value == 101;
//            }
//        }).forEach(System.out::println);

    }

}
