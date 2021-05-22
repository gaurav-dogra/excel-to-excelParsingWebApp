package gmailgdogra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Set;

@SpringBootApplication
public class ExcelToExcelParsingWebAppApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ExcelToExcelParsingWebAppApplication.class, args);
        Set<Officer> officers = UserInput.start("src/main/resources/testFile.xlsx");
        officers.forEach(System.out::println);
    }

}
