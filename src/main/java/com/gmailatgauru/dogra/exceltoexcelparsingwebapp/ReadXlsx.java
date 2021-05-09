package com.gmailatgauru.dogra.exceltoexcelparsingwebapp;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ReadXlsx {

    public void parse(String filePath) throws IOException {
        FileInputStream file = readFile(filePath);
        Workbook workbook = getWorkbook(file);
        Sheet firstSheet = getFirstSheet(workbook);
        printDataFromSheet(firstSheet); // for testing purpose
    }

    private void printDataFromSheet(Sheet firstSheet) {
        System.out.println(firstSheet.getSheetName());
    }

    public FileInputStream readFile(String filePath) throws FileNotFoundException {
        FileInputStream file;
        try {
            file = new FileInputStream(new File(filePath));
            return file;
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
