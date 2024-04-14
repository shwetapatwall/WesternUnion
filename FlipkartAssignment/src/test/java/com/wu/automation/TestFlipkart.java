package com.wu.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

import java.util.List;

public class TestFlipkart {


    WebDriver driver;


    @BeforeMethod
    public void setup() throws InterruptedException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--remote-allow-origins=*");
        WebDriverManager.chromedriver().clearDriverCache().setup();

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com/");
        Thread.sleep(1000);

    }

    @Test
    public void getlinkForEach() {
        List<WebElement> links=driver.findElements(By.tagName("a"));
        for (WebElement e:links) {
            System.out.println(e.getText() + "-" + e.getAttribute("href"));
        }
    }

    @Test
    public void getlinkStream() {
        List<WebElement>links=driver.findElements(By.tagName("a"));
        links.stream()
                .map(e->e.getText()+"-"+e.getAttribute("href"))
                .forEach(System.out::println);
    }

    @Test
    public void getlinkParallelStream() {

        List<WebElement> links = driver.findElements(By.tagName("a"));
        links.parallelStream()
                .filter(e -> !e.getText().isEmpty())
                .forEach(e -> System.out.println(e.getText() + "-" + e.getAttribute("href")));
    }

    @Test
    public void getlinkLambdaExpression() {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        links.forEach(e -> System.out.println(e.getText() + " : " + e.getAttribute("href")));
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}