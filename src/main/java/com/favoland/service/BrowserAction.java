package com.favoland.service;

import com.favoland.data.AmazonProduct;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class BrowserAction {
    private static final String CHROME_DRIVER_PATH = "Driver\\chromedriver.exe";
    public static final String IMAGE_FOLDER = "C:\\Users\\Hosseini\\Desktop\\Favoland Jeff\\UiPath\\HandSoap";

    private static final BrowserAction browserAction = new BrowserAction();

    private BrowserAction() {
    }

    public static BrowserAction getInstance() {
        return browserAction;
    }

    public List<AmazonProduct> browseSingleProductPages(List<String> urls) {
        List<AmazonProduct> productsList = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(urls.get(0));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            urlLoop:
            for (String url : urls) {
                try {
                    System.out.println("*** Product URL: " + url + " ***");
                    driver.get(url);
                    String productName = "";
                    try {
                        productName = driver.findElement(By.id("titleSection")).getText();
                    } catch (NoSuchElementException e) {
                        System.out.println("There is no PRODUCT NAME for this product...");
                    }
                    String description = "";
                    String cost = "";
                    String brand = "";
                    try {
                        description = driver.findElement(By.id("feature-bullets")).getText();
                    } catch (NoSuchElementException e) {
                        System.out.println("There is no DESCRIPTION for this product...");
                    }
                    try {
                        WebElement brandElement = driver.findElement(By.cssSelector("tr.a-spacing-small.po-brand span.a-size-base.po-break-word"));
                        brand = brandElement.getText();
                    } catch (NoSuchElementException e) {
                        System.out.println("There is no BRAND for this product...");
                    }
                    try {
                        WebElement priceElement = driver.findElement(By.cssSelector("span.a-price.a-text-price"));
                        cost = priceElement.getText();
                    } catch (NoSuchElementException e) {
                        System.out.println("There is no PRICE for this product...");
                    }
                    String asinValue = "";
                    String company = "";
                    String countryOfOrigin = "";
                    try {
                        WebElement detailBulletsDiv = driver.findElement(By.id("detailBullets_feature_div"));
                        List<WebElement> listItems = detailBulletsDiv.findElements(By.cssSelector("ul.detail-bullet-list > li"));
                        for (WebElement listItem : listItems) {
                            String listItemText = listItem.getText().trim();
                            if (listItemText.contains("ASIN")) {
                                String[] split = listItemText.split(":");
                                asinValue = split[1];
                            } else if (listItemText.contains("Manufacturer")) {
                                String[] split = listItemText.split(":");
                                company = split[1];

                            } else if (listItemText.contains("Country of origin")) {
                                String[] split = listItemText.split(":");
                                countryOfOrigin = split[1];
                            }
                        }
                    } catch (NoSuchElementException e) {
                        List<WebElement> rows = driver.findElements(By.cssSelector("table#productDetails_detailBullets_sections1 tr"));
                        for (WebElement row : rows) {
                            List<WebElement> cells = row.findElements(By.tagName("td"));
                            if (row.getText().contains("ASIN")) {
                                String[] s = cells.get(0).getText().split(" ");
                                asinValue = s[0];
                            } else if (row.getText().contains("Manufacturer")) {
                                String[] s = cells.get(0).getText().split(" ");
                                company = s[0];
                            }
                        }
                    }
                    saveProductImages(driver, asinValue);
                    AmazonProduct product = AmazonProduct.builder()
                            .ASIN(asinValue)
                            .productName(productName)
                            .company(company)
                            .brand(brand)
                            .description(description)
                            .countryOfOrigin(countryOfOrigin)
                            .URL(url)
                            .cost(cost)
                            .build();
                    productsList.add(product);
                } catch (NoSuchElementException e) {
                    System.out.println("ASIN/COUNTRY OF ORIGIN/MANUFACTURER Missing...");
                    continue urlLoop;
                }
            }
        } finally {
            driver.quit();
            int size = productsList.size();
            System.out.println("Last successful product scraped: " + productsList.get(size - 1).getProductName() + " ASIN:" + productsList.get(size - 1).getASIN());
            return productsList;
        }

    }

    public void saveProductImages(WebDriver driver, String ASIN) {
        for (int i = 5; i < 13; i++) {
            try {
                WebElement element = driver.findElement(By.cssSelector("input[aria-labelledby='a-autoid-" + i + "-announce']"));

                element.click();
                TimeUnit.SECONDS.sleep(2);

                WebElement imageElement = driver.findElement(By.id("landingImage"));
                String imageUrl = imageElement.getAttribute("src");
                if (imageUrl != null) {
                    URL imgUrl = new URL(imageUrl);
                    Path destination = Path.of(IMAGE_FOLDER, "image_" + ASIN + "_" + i + ".jpg");
                    Files.copy(imgUrl.openStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Downloaded image " + i + " to " + destination.toString());
                }

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NoSuchElementException e) {
                System.out.println("No more images available for this product...");
            } catch (ElementNotInteractableException e){
                System.out.println("Catched the exception, continue...");
            }
        }
    }
}
