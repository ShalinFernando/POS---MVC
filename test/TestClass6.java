import javax.swing.*;
import java.io.IOException;
import java.util.logging.*;

public class TestClass6 {

    public static void main(String[] args) throws IOException {

        Logger rootLogger = Logger.getLogger("");

        Logger parentLogger = Logger.getLogger("lk.ijse");
        parentLogger.setUseParentHandlers(false);
        parentLogger.addHandler(new ConsoleHandler());

        Logger logger = Logger.getLogger("lk.ijse.log");
        FileHandler fileHandler = new FileHandler("error.log",true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.log(Level.INFO,"My First Log");

        try {
            throw new RuntimeException("Pissu Double");
        }catch (Exception ex){
            logger.log(Level.SEVERE,"Bayanaka Log Ekak",ex);
        }


    }

}
