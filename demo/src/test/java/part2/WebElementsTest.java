package part2;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test; 

public class WebElementsTest {
    WebDriver driver;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        // driver.close();
    }

    @BeforeMethod
    public void backToHome() {
        driver.get("https://demoqa.com");
    }

    public void scrollToElement(WebElement element) {
        String jsScript = "arguments[0].scrollIntoView(true)";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(jsScript, element);
    }

    public void clickJS(WebElement element) {
        String jsScript = "arguments[0].click();";
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript(jsScript, element);
    }

    @Test
    public void TestingWebElements1() throws InterruptedException {
        // Topic: Scroll To Element Using JavaScriptExecutor
        WebElement formsCard = driver.findElement(By.xpath("//div[@id='app']//h5[text()='Forms']"));
        scrollToElement(formsCard);

        Thread.sleep(1000);
        
        formsCard.click();

        Thread.sleep(1000);

        // Topic: Working With Radio Buttons
        WebElement practiceForm = driver.findElement(By.xpath("//li[@id='item-0']//span[text()='Practice Form']"));
        scrollToElement(practiceForm);

        Thread.sleep(1000);

        practiceForm.click();

        WebElement femaleRadioBtn = driver.findElement(By.id("gender-radio-2"));
        scrollToElement(femaleRadioBtn);

        Thread.sleep(1000);

        // Failure: element click intercepted
        // femaleRadioBtn.click(); (Sometimes .click() method doesn't work !!)
        clickJS(femaleRadioBtn);

        Thread.sleep(1000);

        boolean isFemaleRadioBtnSelected = femaleRadioBtn.isSelected();
        Assert.assertTrue(isFemaleRadioBtnSelected, "\nExpected Female radio button to be selected but it wasn't!\n");

        Thread.sleep(1000);

        // Topic: Working With CheckBoxes
        WebElement sportsBox = driver.findElement(By.id("hobbies-checkbox-1"));
        WebElement readingBox = driver.findElement(By.id("hobbies-checkbox-2"));
        WebElement musicBox = driver.findElement(By.id("hobbies-checkbox-3"));

        // Checking whether selection is working
        if (!sportsBox.isSelected()) {
            scrollToElement(sportsBox);
            Thread.sleep(1000);
            clickJS(sportsBox);

            boolean isSportsBoxSelected = sportsBox.isSelected();
            Assert.assertTrue(isSportsBoxSelected, "\nExpected Sports CheckBox to be checked but it wasn't!\n");
        }

        if (!readingBox.isSelected()) {
            Thread.sleep(1000);
            clickJS(readingBox);

            boolean isReadingBoxSelected = readingBox.isSelected();
            Assert.assertTrue(isReadingBoxSelected, "\nExpected Reading CheckBox to be checked but it wasn't!\n");
        }

        if (!musicBox.isSelected()) {
            Thread.sleep(1000);
            clickJS(musicBox);

            boolean isMusicBoxSelected = musicBox.isSelected();
            Assert.assertTrue(isMusicBoxSelected, "\nExpected Music CheckBox to be checked but it wasn't!\n");
        }

        // Checking whether unselection is working
        if (readingBox.isSelected()) {
            Thread.sleep(1000);
            clickJS(readingBox);

            boolean isReadingBoxSelected = readingBox.isSelected();
            Assert.assertFalse(isReadingBoxSelected, "\nExpected Reading CheckBox to be unchecked but it wasn't!\n");
        }
    } 
    
    @Test
    public void TestingWebElements2() throws InterruptedException {
        // Topic: Working With Tables
        WebElement elementsCard = driver.findElement(By.xpath("//div[@id='app']//h5[text()='Elements']"));
        scrollToElement(elementsCard);

        Thread.sleep(1000);

        elementsCard.click();

        WebElement webTables = driver.findElement(By.xpath("//li[@id='item-3']//span[text()='Web Tables']"));
        scrollToElement(webTables);

        Thread.sleep(1000);

        webTables.click();

        String email = "alden@example.com";
        WebElement editBtn2 = driver.findElement(By.xpath("//div[text()='" + email + "']/ancestor::div[@class='rt-tr-group']//span[@title='Edit']"));

        editBtn2.click();

        Thread.sleep(1000);

        WebElement ageField = driver.findElement(By.id("age"));
        ageField.clear();
        String expectedAge = "19";
        ageField.sendKeys(expectedAge);

        Thread.sleep(1000);
        WebElement submitBtn = driver.findElement(By.id("submit"));
        submitBtn.click();

        String aldenAge = driver.findElement(By.xpath("//div[text()='" + email + "']/ancestor::div[@class='rt-tr-group']//div[text()='" + expectedAge + "']")).getText();
        Assert.assertEquals(aldenAge, expectedAge, "\nExpected Age to be " +  expectedAge + " but it wasn't!\n");

        Thread.sleep(1000);

        // Topic: Working With Links
        WebElement links = driver.findElement(By.xpath("//li[@id='item-5']//span[text()='Links']"));
        scrollToElement(links);

        Thread.sleep(1000);

        links.click();

        WebElement badRequestLink = driver.findElement(By.id("bad-request"));
        scrollToElement(badRequestLink);

        Thread.sleep(1000);

        badRequestLink.click();

        Thread.sleep(1000);

        List<WebElement> responseText = driver.findElements(By.xpath("//p[@id='linkResponse']//b"));
        String[] responseB = new String[] { responseText.get(0).getText(), responseText.get(1).getText() };
        String[] expected = new String[] { "400", "Bad Request" };

        Assert.assertTrue(responseB[0].equals(expected[0]) && responseB[1].equals(expected[1]), "Actual Response: " + responseB[0] + "-" + responseB[1] + "\nExpected: " + expected[0] + "-" + expected[1]);
    }

    @Test
    public void TestingWebElements3() throws InterruptedException {
        // Topic: Working With Drop Downs
        WebElement widgetsCard = driver.findElement(By.xpath("//div[@class='card-body']//h5[text()='Widgets']"));
        scrollToElement(widgetsCard);

        Thread.sleep(1000);

        widgetsCard.click();

        Thread.sleep(1000);

        WebElement selectMenu = driver.findElement(By.xpath("//li[@id='item-8']//span[text()='Select Menu']"));
        scrollToElement(selectMenu);

        Thread.sleep(1000);

        selectMenu.click();

        Thread.sleep(1000);

        WebElement standardMultiSelect = driver.findElement(By.id("cars"));
        scrollToElement(standardMultiSelect);

        Thread.sleep(1000);

        Select options = new Select(standardMultiSelect);
        options.selectByVisibleText("Volvo");
        options.selectByIndex(1);
        options.selectByVisibleText("Opel");
        options.selectByIndex(3);

        options.deselectByValue("saab");

        List<WebElement> selectedOptions = options.getAllSelectedOptions();
        List<String> selectedOptionsStrings = selectedOptions.stream().map(WebElement::getText).collect(Collectors.toList());

        Assert.assertTrue(selectedOptionsStrings.contains("Volvo"));

        Assert.assertFalse(selectedOptionsStrings.contains("Saab"), "\nExpected 'Saab' to be unselected but it wasn't\n");

        Assert.assertTrue(selectedOptionsStrings.contains("Opel"));
        Assert.assertTrue(selectedOptionsStrings.contains("Audi"));
    }
}
