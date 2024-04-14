package com.wu.automation;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Workbook;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMoneyRediff {


    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://money.rediff.com/losers/bse/daily/groupall");
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void validateWebTable() throws IOException {

        String FilePath = System.getProperty("user.dir")+"/src/test/resources/data/DailyStock.xlsx";
        Workbook workbook = null;


        Map<String, String> excelData = new HashMap<>();
        Map<String, String> webtableData = new HashMap<>();


        //Read the data from webpage
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@class =\"dataTable\"]")));

        List<WebElement> rows = table.findElements(By.tagName("tr"));

        for (int i = 1; i < rows.size(); i++) {
            List<WebElement> column = rows.get(i).findElements(By.tagName("td"));
            String key = column.get(0).getText();
            String value = column.get(3).getText();
            webtableData.put(key, value);
        }

        //Read data from excelsheet
        try {
            workbook = new XSSFWorkbook(new FileInputStream(FilePath));
        } catch (IOException e) {
            e.printStackTrace();

            throw new IOException("Unsupported file format XLSX file.");
        }

        if(workbook!=null) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                Cell stock = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell price = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                excelData.put(stock.toString(), price.toString());
            }

            workbook.close();
        }



        MatcherAssert.assertThat(webtableData, Matchers.equalTo(excelData));


    }
}
