package gmailgdogra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ExcelToExcelParsingWebApp {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(ExcelToExcelParsingWebApp.class, args);
        File file = new File("src/main/resources/testFile.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        List<SwipeRecord> allSwipes = ReadXlsxService.parse(inputStream);
        List<SwipeRecord> outputData = SwipeProcessor.getOutputDataFrom(allSwipes);
        outputData.forEach(System.out::println);
        WriteOutputXlsx.write(outputData);
    }
}
