package com.favoland.service;

import com.favoland.data.AmazonProduct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelAction {
    private static final ExcelAction EXCEL_ACTION = new ExcelAction();

    private ExcelAction() {
    }

    public static ExcelAction getInstance() {
        return EXCEL_ACTION;
    }


    public static final String URL_LIST = "C:\\Users\\Hosseini\\Desktop\\FAVOLAND\\UiPath\\HandSoap\\HandSoap.1.xlsx";
    public static final String SCRAPE_PRODUCT_FILE_PATH = "C:\\Users\\Hosseini\\Desktop\\FAVOLAND\\UiPath\\HandSoap\\ScrapedData.HandSoap.1.xlsx";

    public static final String ALL_PRODUCTS_LIST = "C:\\Users\\Hosseini\\Desktop\\FAVOLAND\\Amazon.xlsx";

    public static void writeIngredients(String url, String ingredientsText) {
        try (FileInputStream fileInputStream = new FileInputStream(ALL_PRODUCTS_LIST);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell urlCell = row.getCell(11);
                if (urlCell != null && urlCell.getCellType() == CellType.STRING
                        && urlCell.getStringCellValue().equals(url)) {
                    Cell ingredientsCell = row.createCell(10);
                    ingredientsCell.setCellValue(ingredientsText);
                    break;
                }
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(ALL_PRODUCTS_LIST)) {
                workbook.write(fileOutputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<String> readURLs(int column){
        List<String> allURLs = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(ALL_PRODUCTS_LIST);
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            fileInputStream.close();
            Sheet sheet = workbook.getSheetAt(0);

            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if(row == null)
                    continue;
                Cell amountCell = row.getCell(column);

                String URL = amountCell.getStringCellValue();
                allURLs.add(URL);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allURLs;
    }

    public void writeProductsInExcelSheet(List<AmazonProduct> products) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Product Name");
            headerRow.createCell(1).setCellValue("Brand");
            headerRow.createCell(2).setCellValue("Company");
            headerRow.createCell(3).setCellValue("Cost");
            headerRow.createCell(4).setCellValue("ASIN");
            headerRow.createCell(5).setCellValue("UPC");
            headerRow.createCell(6).setCellValue("Description");
            headerRow.createCell(7).setCellValue("Country Of Origin");
            headerRow.createCell(8).setCellValue("Product Link");

            for (int i = 0; i < products.size(); i++) {
                Row row = sheet.createRow(i + 1);
                AmazonProduct product = products.get(i);

                row.createCell(0).setCellValue(product.getProductName());
                row.createCell(1).setCellValue(product.getBrand());
                row.createCell(2).setCellValue(product.getCompany());
                row.createCell(3).setCellValue(product.getCost());
                row.createCell(4).setCellValue(product.getASIN());
                row.createCell(5).setCellValue(product.getUPC());
                row.createCell(6).setCellValue(product.getDescription());
                row.createCell(7).setCellValue(product.getCountryOfOrigin());
                row.createCell(8).setCellValue(product.getURL());
            }

            try (FileOutputStream fileOut = new FileOutputStream(SCRAPE_PRODUCT_FILE_PATH)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
