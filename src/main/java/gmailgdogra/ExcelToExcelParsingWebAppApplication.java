package gmailgdogra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ExcelToExcelParsingWebAppApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ExcelToExcelParsingWebAppApplication.class, args);
        new UserInput().start();
    }

}
