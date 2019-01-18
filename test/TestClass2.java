import lk.ijse.dep.app.entity.CustomEntity;
import lk.ijse.dep.app.entity.Customer;
import lk.ijse.dep.app.entity.Item;
import lk.ijse.dep.app.entity.SuperEntity;

class A{

    void printSomething(SuperEntity entity){
        System.out.println("Super Entity");
    }

    public A(){
        System.out.println(this);
    }

}

class B extends  A{

    void printSomething(Customer entity){
        System.out.println("Customer Entity");
    }

    public B(){
        System.out.println(this);
    }

}

public class TestClass2 {

    public static void main(String[] args) {
        B b = new B();
        SuperEntity superEntity = new Customer();
        b.printSomething(superEntity);

    }

}
