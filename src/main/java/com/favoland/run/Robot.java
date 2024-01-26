package com.favoland.run;

import com.favoland.data.AmazonProduct;
import com.favoland.service.BrowserAction;
import com.favoland.service.ExcelAction;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Robot {
    private static final BrowserAction browserAction = BrowserAction.getInstance();
    private static final ExcelAction excelAction = ExcelAction.getInstance();


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        List<String> urls = excelAction.readURLs();
        //browserAction.browseUrlForImage(urls);
        List<AmazonProduct> products = browserAction.browseSingleProductPages(urls);
        excelAction.writeProductsInExcelSheet(products);
        System.out.println("#" + products.size() + " Products Scraped");

        long finishTime = System.currentTimeMillis();
        long duration = finishTime - startTime;
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = (TimeUnit.MILLISECONDS.toMinutes(duration)) % 60;
        long seconds = (TimeUnit.MILLISECONDS.toSeconds(duration) % 60);
        System.out.format("Total Time " + hours + ":" + minutes + ":" + seconds);
    }
}
