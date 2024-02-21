package com.favoland.run;

import com.favoland.service.BrowserAction;
import com.favoland.service.ExcelAction;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Robot {
    private static final BrowserAction browserAction = BrowserAction.getInstance();
    private static final ExcelAction excelAction = ExcelAction.getInstance();
    public static final int START_ROW = 1;
    public static final int NUM_OF_URLS = 28000;
    public static final int URL_COLUMN = 11;


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        //To scrape the images from urls
        /*List<String> urls = excelAction.readURLs(0);//use URL_LIST in ExcelAction
        browserAction.browseUrlForImage(urls);*/

        //To scrape data from urls
        //List<String> urls = excelAction.readURLs(0);//use URL_LIST in ExcelAction
        /*List<AmazonProduct> products = browserAction.browseSingleProductPages(urls);
        excelAction.writeProductsInExcelSheet(products);
        System.out.println("#" + products.size() + " Products Scraped");*/

        //To scrape ingredients
        int startRow = START_ROW;
        List<String> urls = excelAction.readURLs(URL_COLUMN, startRow, NUM_OF_URLS);
        try {
            browserAction.getIngredientsFromUrl(urls, startRow);
        } catch (org.openqa.selenium.WebDriverException e) {
            System.err.println("WebDriverException");
        }
        long finishTime = System.currentTimeMillis();
        long duration = finishTime - startTime;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = (TimeUnit.MILLISECONDS.toMinutes(duration)) % 60;
        long seconds = (TimeUnit.MILLISECONDS.toSeconds(duration) % 60);
        System.out.format("Total Time " + hours + ":" + minutes + ":" + seconds);
    }
}
