import java.util.Optional;

public class TestClass3 {

    public static void main(String[] args) {
        String name = "IJSE";
        Optional<String> name1 = Optional.ofNullable(name);
        System.out.println(name1.isPresent());
    }

}
