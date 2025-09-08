package part3;

import java.time.LocalDate;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.interactions.Actions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.ITestResult;

public class SeleniumInteractions {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        // driver.quit();
    }

    @BeforeMethod
    public void backToHome() {
        driver.get("https://demoqa.com");
    }

    @AfterMethod
    public void failedResultScreenshot(ITestResult testResult) {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            TakesScreenshot scrnshot = (TakesScreenshot)driver;

            File src = scrnshot.getScreenshotAs(OutputType.FILE);
            File dst = new File(System.getProperty("user.dir") + "/resources/screenshots/(" + LocalDate.now() + ")" + testResult.getName() + ".png");

            try {
                FileHandler.copy(src, dst);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Screenshot Located At " + dst);
        }
    }
    
    public void scrollToElement(WebElement element) {
        String jsScript = "arguments[0].scrollIntoView(true)";

        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(jsScript, element);
    }

    public void goToWidgetsPage() {
        WebElement widgetsCard = driver.findElement(By.xpath("//div[@class='card-body']/h5[text()='Widgets']"));
        scrollToElement(widgetsCard);

        widgetsCard.click();
    }

    public void goToElementsPage() {
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']/h5[text()='Elements']"));
        scrollToElement(elementsCard);

        elementsCard.click();
    }

    // Topic: Simulating Mouse Movements
    @Test
    public void testingForASlider() {
        goToWidgetsPage();

        WebElement sliderPage = driver.findElement(By.xpath("//li[@id='item-3']/span[text()='Slider']"));
        scrollToElement(sliderPage);

        sliderPage.click();

        WebElement slider = driver.findElement(By.xpath("//input[@type='range']"));
        scrollToElement(slider);

        Actions act = new Actions(driver);
        int x = 180, y = 0;
        act.dragAndDropBy(slider, x, y).perform();

        String sliderValue = driver.findElement(By.id("sliderValue")).getAttribute("value");
        String expectedValue = "85"; 

        Assert.assertEquals(sliderValue, expectedValue, "\nActual & Expected Values Do Not Match\n");
    }

    // Topic: Simulating Keyboard Events
    @Test 
    public void testingUsingKeyboard() {
        goToElementsPage();

        WebElement textBoxPage = driver.findElement(By.xpath("//li[@id='item-0']/span[text()='Text Box']"));
        scrollToElement(textBoxPage);

        textBoxPage.click();

        WebElement fullName = driver.findElement(By.id("userName"));
        scrollToElement(fullName);

        String name = "Sita Ram";
        String email = "ganeshvamsi@gmail.com";
        String addressPart1 = "AP";
        String addressPart2 = "India";

        Actions act = new Actions(driver);
        act.click(fullName).sendKeys(name).perform();
        // Now there is no need for inspecting other input fields !! ....... 
        // We can just use Keyboard through 'act' !
        act.sendKeys(Keys.TAB).pause(1000).sendKeys(email).perform();
        act.sendKeys(Keys.TAB).pause(1000).sendKeys(addressPart1).pause(1000).sendKeys(Keys.ENTER).sendKeys(addressPart2).perform();

        WebElement submitBtn = driver.findElement(By.id("submit"));
        scrollToElement(submitBtn);

        submitBtn.click();

        String output = driver.findElement(By.xpath("//p[@id='currentAddress']")).getText();
        System.out.println(output);
        Assert.assertTrue(output.contains(addressPart1));
    }
}
