package lk.ijse.dep.app.business.custom;

import lk.ijse.dep.app.business.SuperBO;
import lk.ijse.dep.app.dto.CustomerDTO;

import java.util.List;

public interface ManageCustomersBO extends SuperBO {

    List<CustomerDTO> getCustomers() throws Exception;

    boolean createCustomer(CustomerDTO dto) throws Exception;

    boolean updateCustomer(CustomerDTO dto) throws Exception;

    boolean deleteCustomer(String customerID) throws Exception;

    CustomerDTO findCustomer(String id) throws Exception;


}
