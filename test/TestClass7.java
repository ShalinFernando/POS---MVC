import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TestClass7 {

    public static void main(String[] args) throws IOException {

        Properties properties = new Properties();

        File file = new File("resource/application.properties");
        FileReader fileReader = new FileReader(file);

        properties.load(fileReader);

        System.out.println(properties.getProperty("ip"));

    }

}
