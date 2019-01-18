import java.awt.*;

public class TestClass {

    public void abc(){
        System.out.println("Super Class" + this);

        GoodGirl girl1 = new GoodGirl() {
            @Override
            public void kiss(String something,int a) {
                System.out.println("Inner Class : " + this);
                printSomething(something,a);
            }
        };
        girl1.kiss("IJSE",10);

        GoodGirl girl2 = (something,a)->{
            printSomething(something,a);
            System.out.println("Lambda Expression : " + this);
        };
        girl2.kiss("ESOFT",10);

        GoodGirl girl3 = TestClass::printSomething;
        girl3.kiss("NSBM",10);

    }

    public static void printSomething(String something, int number){
        System.out.println(something);
        System.out.println(number);
    }

    public TestClass(){
    }

    public static void main(String[] args) {
        new TestClass().abc();
    }

}
