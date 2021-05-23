package gmailgdogra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ExcelToExcelParsingWebApp {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ExcelToExcelParsingWebApp.class, args);
        List<TransactionRecord> outputData =
                PrepareOutputData.from("src/main/resources/testFile.xlsx");
        outputData.forEach(System.out::println);
    }

}
