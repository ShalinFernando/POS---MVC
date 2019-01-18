import lk.ijse.dep.app.business.Converter;
import lk.ijse.dep.app.dto.CustomerDTO;
import lk.ijse.dep.app.dto.SuperDTO;
import lk.ijse.dep.app.entity.Customer;

import java.util.ArrayList;
import java.util.List;

public class TestClass5 {

    public static void main(String[] args) {

        List<Customer> c = new ArrayList<>();
//        c.add(new Customer());
        List<CustomerDTO> dtoList = Converter.<CustomerDTO>getDTOList(c);
        System.out.println(dtoList);
    }

}
