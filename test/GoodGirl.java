import lk.ijse.dep.app.entity.Customer;
import lk.ijse.dep.app.entity.SuperEntity;

//@FunctionalInterface
public interface GoodGirl extends Girl{

    void kiss(String something,int a);

//    int printSomething(Customer a);

    default void test(){

    }

    static void testing(){

    }

}
