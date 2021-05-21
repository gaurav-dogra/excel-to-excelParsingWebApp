package gmailgdogra;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadXlsx {

    public void parse(String filePath) throws IOException {

        try(FileInputStream fileStream = getFileInputStream(filePath)) {
            Workbook workbook = getWorkbook(fileStream);
            Sheet firstSheet = getFirstSheet(workbook);
            printDataFromSheet(firstSheet); // for testing purpose
        }
    }

    private void printDataFromSheet(Sheet firstSheet) {
        System.out.println(firstSheet.getSheetName());
    }

    public FileInputStream getFileInputStream(String filePath) throws IOException {

        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException(filePath + " not found");
        }
    }

    public Workbook getWorkbook(FileInputStream file) throws IOException {
        Workbook workbook;
            try {
                workbook = new XSSFWorkbook(file);
                return workbook;
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Not able to find the workbook");
            }
    }

    public Sheet getFirstSheet(Workbook workbook) {
        return workbook.getSheetAt(0);
    }
}
