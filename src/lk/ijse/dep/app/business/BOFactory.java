package lk.ijse.dep.app.business;

import lk.ijse.dep.app.business.custom.impl.ManageCustomersBOImpl;
import lk.ijse.dep.app.business.custom.impl.ManageItemsBOImpl;
import lk.ijse.dep.app.business.custom.impl.ManageOrdersBOImpl;

public class BOFactory {

    public enum BOTypes{
        MANAGE_CUSTOMERS,MANAGE_ITEMS,MANAGE_ORDERS
    }

    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getInstance(){
        if (boFactory == null){
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public <T extends SuperBO> T getBO(BOTypes boType){
        switch (boType){
            case MANAGE_CUSTOMERS:
                return (T) new ManageCustomersBOImpl();
            case MANAGE_ITEMS:
                return (T) new ManageItemsBOImpl();
            case MANAGE_ORDERS:
                return (T) new ManageOrdersBOImpl();
            default:
                return null;
        }
    }

}
