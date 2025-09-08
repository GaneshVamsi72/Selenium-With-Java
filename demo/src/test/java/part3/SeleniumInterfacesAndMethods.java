package part3;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import java.util.List;
import java.util.Set;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SeleniumInterfacesAndMethods {
    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();

        /* Implicit Wait 
        1. Tells WebDriver to wait for a certain amount of time when trying to find an element if it’s not immediately available.
        2. It is applied globally (for all elements).
        */

        // driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));

        /* Not recommended for complex test cases because it’s applied globally. 
        Warning from Documentation of Selenium: Do not mix implicit and explicit waits. Doing so can cause unpredictable wait times. 
        For example, setting an implicit wait of 10 seconds and an explicit wait of 15 seconds could cause a timeout to occur after 20 seconds.
        */
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @BeforeMethod
    public void backToHome() {
        driver.get("https://demoqa.com/");
    }

    @AfterMethod
    public void failedResultScreenshot(ITestResult testResult) {
        if (ITestResult.FAILURE == testResult.getStatus()) {
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

    // Topic: Taking a Screenshot
    @Test
    public void takeAScreenshotDemo() {
        WebElement formsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Forms']"));
        scrollToElement(formsCard);

        formsCard.click();

        WebElement practiceForm = driver.findElement(By.xpath("//li[@id='item-0']//span[text()='Practice Form']"));
        scrollToElement(practiceForm);

        practiceForm.click();

        WebElement submitBtn = driver.findElement(By.id("submit"));
        submitBtn.click(); // Intentional Test Failure !! (Clicking without scrolling to the element using JavascriptExecutor)
    }

    // Topic: Handling A Modal
    @Test
    public void testingModalDialog() {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement modalDialogs = driver.findElement(By.xpath("//li[@id='item-4']//span[text()='Modal Dialogs']"));
        scrollToElement(modalDialogs);

        modalDialogs.click();

        WebElement smallModalBtn = driver.findElement(By.id("showSmallModal"));
        scrollToElement(smallModalBtn);

        smallModalBtn.click();

        String modalBody = driver.findElement(By.xpath("//div[contains(text(), 'small modal')]")).getText();

        Assert.assertTrue(modalBody.contains("small modal"));
        
        WebElement closeBtn = driver.findElement(By.id("closeSmallModal"));
        closeBtn.click();
    }

    // Topic: Handling Alerts 
    // For an alert, we are not allowed to inspect the message or button... Because alerts are not attached to the DOM !
    // But Selenium provides special methods for switching to alerts and performing actions on them and go back to main page !
    @Test
    public void testingInformationAlerts() {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement alerts = driver.findElement(By.xpath("//li[@id='item-1']//span[text()='Alerts']"));
        scrollToElement(alerts);

        alerts.click();

        WebElement infoAlertBtn = driver.findElement(By.id("alertButton"));
        scrollToElement(infoAlertBtn);

        infoAlertBtn.click();

        Alert infoAlert = driver.switchTo().alert();
        String alertInfo = infoAlert.getText();
        String expectedInfo = "You clicked a button";

        Assert.assertEquals(alertInfo, expectedInfo, "\nActual and Expected messages do not match !\n");

        infoAlert.accept();
    }

    @Test
    public void testingConfirmationAlerts() {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement alerts = driver.findElement(By.xpath("//li[@id='item-1']//span[text()='Alerts']"));
        scrollToElement(alerts);

        alerts.click();

        WebElement confirmAlertBtn = driver.findElement(By.id("confirmButton"));
        scrollToElement(confirmAlertBtn);

        confirmAlertBtn.click();

        Alert confirmAlert = driver.switchTo().alert();
        confirmAlert.dismiss();

        String confirmResult = driver.findElement(By.id("confirmResult")).getText();
        String expectedResult = "You selected Cancel";
        
        Assert.assertEquals(confirmResult, expectedResult, "\nExpected 'Cancel' button to be clicked but it wasn't !\n");        
    }

    @Test
    public void testingPromptingAlerts() {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement alerts = driver.findElement(By.xpath("//li[@id='item-1']//span[text()='Alerts']"));
        scrollToElement(alerts);

        alerts.click();

        WebElement promptAlertBtn = driver.findElement(By.id("promptButton"));
        scrollToElement(promptAlertBtn);

        promptAlertBtn.click();

        Alert prompAlert = driver.switchTo().alert();
        String key = "SitaRam";
        prompAlert.sendKeys(key);
        prompAlert.accept();

        String promptResult = driver.findElement(By.id("promptResult")).getText();
        String expectedResult = "You entered" + key;
        Assert.assertEquals(promptResult, expectedResult, "\nActual and Expected Results do not match\n");
    }

    // Topic: Frames
    @Test
    public void testingFrames1() {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement frames = driver.findElement(By.xpath("//li[@id='item-2']//span[text()='Frames']"));
        scrollToElement(frames);

        frames.click();

        // What's so special about frames??.. Well here is the answer
        /*
        In Selenium, `switchTo().frame()` is mandatory when dealing with frames/iframes because:

        1. WebDriver always works in the current context
            -> By default, Selenium looks for elements in the main HTML document.
            -> If the element you want is inside a `<frame>` or `<iframe>`, Selenium cannot see it unless you "enter" that frame.

        2. Frames are like separate pages inside a page
            -> Think of a frame/iframe as a small browser window embedded in the main page.
            -> To interact with anything inside it, you must tell Selenium: → "Go inside this frame first."

        3. Without `switchTo().frame()`, Selenium throws `NoSuchElementException`
            -> Because Selenium keeps searching in the main DOM, not inside the iframe.

        4. Context switching flexibility
            -> `driver.switchTo().frame(index/name/WebElement)` → enter frame
            -> `driver.switchTo().defaultContent()` → go back to main page
            -> `driver.switchTo().parentFrame()` → go one level up

        Example:

        // Without switching → will fail
        driver.findElement(By.id("username")).sendKeys("Vamsi");

        // Correct way
        driver.switchTo().frame("loginFrame");
        driver.findElement(By.id("username")).sendKeys("Vamsi");
        driver.switchTo().defaultContent(); // back to main page

        ⚡ In short:
        We use `switchTo()` for frames in Selenium because frames isolate their DOM from the main page, and Selenium needs to switch context to interact with elements inside them.
        */
        // Switching using WebElement !
        driver.switchTo().frame(driver.findElement(By.id("frame1")));
        String frame1Header = driver.findElement(By.id("sampleHeading")).getText();
        String expectedHeader = "This is a sample page";

        Assert.assertEquals(frame1Header, expectedHeader, "\nActual & Expected Headers do not match!\n");
        driver.switchTo().defaultContent();
    }

    @Test
    public void testingFrames2() throws InterruptedException {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement frames = driver.findElement(By.xpath("//li[@id='item-2']//span[text()='Frames']"));
        scrollToElement(frames);

        frames.click();

        Thread.sleep(4000); // Intentionally delayed !!!!! so that frame gets time for the loading !

        // Below five lines are for the clarity of the total frames (including ads frames and all !!) in the web page
        List<WebElement> allFrames = driver.findElements(By.tagName("iframe"));
        System.out.println("Total iframes: " + allFrames.size());
        for (int i = 0; i < allFrames.size(); i++) {
            System.out.println(i + " -> " + allFrames.get(i).getAttribute("id"));
        }

        // Switching using index !
        driver.switchTo().frame(3); // Small frame in the web page
        String frame2Header = driver.findElement(By.id("sampleHeading")).getText();
        String expectedHeader = "This is a sample page";

        Assert.assertEquals(frame2Header, expectedHeader, "\nActual & Expected  Headers do not match!\n");
        driver.switchTo().defaultContent();
    }

    // Topic: Windows handling (switch between tabs)
    @Test
    public void testingNewWindowURL() throws InterruptedException {
        WebElement modalsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Alerts, Frame & Windows']"));
        scrollToElement(modalsCard);

        modalsCard.click();

        WebElement browserWindows = driver.findElement(By.xpath("//li[@id='item-0']//span[text()='Browser Windows']"));
        scrollToElement(browserWindows);

        browserWindows.click();

        WebElement newWindowBtn = driver.findElement(By.id("windowButton"));
        scrollToElement(newWindowBtn);

        newWindowBtn.click();

        // Switching To New Window
        // Step 1: Get The Current 'Main' Window Handle
        String mainHandle = driver.getWindowHandle();
        System.out.println("Main Window ID" + mainHandle);

        // Step 2: Get All Open Windows' Handles
        Set<String> allHandles = driver.getWindowHandles();
        System.out.println("Total Open Windows: " + allHandles.size());
        for (String handle : allHandles) {
            if (!mainHandle.equals(handle)) {
                // Switch Selenium’s focus to a particular window
                driver.switchTo().window(handle);
                System.out.println(handle);
            }
        }

        String curURL = driver.getCurrentUrl();
        String expectedURL = "https://demoqa.com/sample";
        Assert.assertEquals(curURL, expectedURL, "Actual & Expected URL's Do Not Match\n");

        Thread.sleep(1000);
        driver.close();
        driver.switchTo().window(mainHandle);
    }

    // Topic: Dynamic Waits
    /*
    1. A dynamic wait means Selenium will wait only as long as needed for a condition to be true (element to be visible, clickable, etc.).
    2. If the condition is satisfied before the max timeout, execution continues immediately.
    3. Unlike static wait (Thread.sleep()), dynamic waits are smarter → they don’t block unnecessarily.
    */
    @Test
    public void testingUsingDynamicWaits1() {
        WebElement elementsCard = driver.findElement(By.xpath("//div[@class='card-body']/h5[text()='Elements']"));
        scrollToElement(elementsCard);

        elementsCard.click();

        WebElement dynamicProperties = driver.findElement(By.xpath("//li[@id='item-8']/span[text()='Dynamic Properties']"));
        scrollToElement(dynamicProperties);

        dynamicProperties.click();

        /*
        Explicit Wait (WebDriverWait)
        1. Waits for a specific condition (like visibility, clickability, presence of element).
        2. Applied only on specific elements.
        */
        By locatorOfVisAfterBtn = By.id("visibleAfter");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfVisAfterBtn));

        WebElement visibleAfterBtn = driver.findElement(locatorOfVisAfterBtn);

        String btnText = visibleAfterBtn.getText();
        String expectedText = "Visible After 5 Seconds";

        Assert.assertEquals(btnText, expectedText, "Actual & Expected Text Do No Match");
    }

    @Test
    public void testingUsingDynamicWaits2() {
        WebElement widgetsCard = driver.findElement(By.xpath("//div[@class='card-body']/h5[text()='Widgets']"));
        scrollToElement(widgetsCard);

        widgetsCard.click();

        WebElement progressBar = driver.findElement(By.xpath("//li[@id='item-4']/span[text()='Progress Bar']"));
        scrollToElement(progressBar);

        progressBar.click();

        WebElement startStopBtn = driver.findElement(By.id("startStopButton"));
        scrollToElement(startStopBtn);

        startStopBtn.click();

        By locatorOfResetBtn = By.id("resetButton");
        
        // Fluent Wait - A type of explicit wait with polling frequency (how often to check) and can ignore exceptions.
        
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(20)) // max wait time
                .pollingEvery(Duration.ofSeconds(2)) // check every 2 sec
                .ignoring(NoSuchElementException.class); // ignore this exception
        wait.until(ExpectedConditions.visibilityOfElementLocated(locatorOfResetBtn));
        
        WebElement resetBtn = driver.findElement(locatorOfResetBtn);
        scrollToElement(resetBtn);

        resetBtn.click();

        String resetProgress = driver.findElement(By.xpath("//div[@id='progressBar']/div")).getText();
        String expectedProgress = "0%";

        Assert.assertEquals(resetProgress, expectedProgress, "Expected Progress after Reset is 0% but it wasn't !");
    }
}