package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
    ChromeDriver driver;
    //Wrappers wrappers = new Wrappers();

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */
    @Test
    public void testCase01() {
        Boolean status;
        Wrappers.logStatus("Start TestCase", "Test Case 1:", "DONE");
        driver.get("https://www.youtube.com/");
        status = driver.getCurrentUrl().equalsIgnoreCase("https://www.youtube.com/");
        Assert.assertTrue(status, "Landing on You tube home page failed");

        //Click on Hamburger menu
        Wrappers.clickAction(driver, By.xpath("//button[@aria-label='Guide']"));
        //WebElement guideMenu = driver.findElement(By.xpath("//button[@aria-label='Guide']"));

        //Click on about
        Wrappers.clickAction(driver, By.xpath("//a[text()='About']"));
        status = Wrappers.waitForPageLoad(driver, "about");
        Assert.assertTrue(status, "Landing on about page failed");

        //Get message displayed on about page
        String message = Wrappers.getElemetText(driver, By.xpath("//section[@class='ytabout__content']"));
        System.out.println("About page message : " + message);

        Wrappers.logStatus("End TestCase", "Test Case 1:", status ? "PASS" : "FAIL");
    }

    @Test
    public void testCase02() throws InterruptedException {
        Boolean status;
        Wrappers.logStatus("Start TestCase", "Test Case 2:", "DONE");
        driver.get("https://www.youtube.com/");
        status = driver.getCurrentUrl().equalsIgnoreCase("https://www.youtube.com/");
        Assert.assertTrue(status, "Landing on You tube home page failed");

        //Click on Hamburger menu
        Wrappers.clickAction(driver, By.xpath("//button[@aria-label='Guide']"));
        // WebElement guideMenu = driver.findElement(By.xpath("//button[@aria-label='Guide']"));

        Wrappers.clickAction(driver, By.xpath("//a[@title='Movies']"));
        //Thread.sleep(2000);

        Wrappers.scrollToRightExtreem(driver,
                By.xpath("//span[text()='Top selling']/ancestor::div[@id='title-container']/../following-sibling::div//div[@id='right-arrow']//button"));

        String movieType = Wrappers.getLastElemetTextInList(driver,By.xpath
                ("//span[text()='Top selling']/ancestor::div[@id='title-container']/../following-sibling::div//span[contains(@class,'grid-movie-renderer-metadata ')]"));
        SoftAssert sa= new SoftAssert();
        sa.assertTrue(movieType.contains("Comedy") || movieType.equals("Animation"), "Movie is neither 'Comedy' nor 'Animation'");

        String movieCertification = Wrappers.getLastElemetTextInList(driver,By.xpath
                ("//span[text()='Top selling']/ancestor::div[@id='title-container']/../following-sibling::div//p[not(text()='Buy or rent') and not(text()='Rent')]"));
        sa.assertEquals(movieCertification, "A", "Movie is not marked 'A' for Mature");

    }

    @Test
    public void testCase03() throws InterruptedException {
        Boolean status;
        Wrappers.logStatus("Start TestCase", "Test Case 1:", "DONE");
        driver.get("https://www.youtube.com/");
        status = driver.getCurrentUrl().equalsIgnoreCase("https://www.youtube.com/");
        Assert.assertTrue(status, "Landing on You tube home page failed");

        //Click on Hamburger menu
        Wrappers.clickAction(driver, By.xpath("//button[@aria-label='Guide']"));

        //Click on Music button/link text
        Wrappers.clickAction(driver,By.xpath("//a[@title='Music']"));

        String title = Wrappers.getElemetText(driver,By.xpath
                ("//span[(@id='title') and (@class='style-scope ytd-shelf-renderer')]"));

        Wrappers.scrollToRightExtreem(driver,By.xpath
                ("//span[text()=\""+title+"\"]/ancestor::div[@id='title-container']/../following-sibling::div[@id='contents']//div[@id='right-arrow']//button"));

        String totalTracks = Wrappers.getLastElemetTextInList(driver,By.xpath(
                "//span[text()=\""+title+"\"]/ancestor::div[@id='title-container']/../following-sibling::div[@id='contents']//p[@id='video-count-text']"));

        String[] splitTotalTracks = totalTracks.split(" ");

        SoftAssert sa = new SoftAssert();
        sa.assertTrue(Wrappers.convertToNumericValue(splitTotalTracks[0])<=50,"Tracks are more than 50");
    }

    @Test
    public void testCase04(){
        Boolean status;
        Wrappers.logStatus("Start TestCase", "Test Case 1:", "DONE");
        driver.get("https://www.youtube.com/");
        status = driver.getCurrentUrl().equalsIgnoreCase("https://www.youtube.com/");
        Assert.assertTrue(status, "Landing on You tube home page failed");

        //Click on Hamburger menu
        Wrappers.clickAction(driver, By.xpath("//button[@aria-label='Guide']"));

        //Click on News button/link text
        Wrappers.clickAction(driver,By.xpath("//a[@title='News']"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement latestNewTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Latest news posts']/ancestor::div[@id='rich-shelf-header-container']/following-sibling::div[@id='contents']")));

        List<WebElement> latestNews = latestNewTab.findElements(By.xpath(".//div[(@id='content') and (@class='style-scope ytd-rich-item-renderer')]"));
        if(latestNews.size()<=3){
            int numberOfVotes =0;
            for(int i=0;i<latestNews.size();i++){
                System.out.println("News header"+ i +": "+ Wrappers.getElementFromParentElementAndPrintText(driver,latestNewTab,By.xpath(".//div[(@id='header') ]"),i));
                System.out.println("News Body"+ i +": "+ Wrappers.getElementFromParentElementAndPrintText(driver,latestNewTab,By.xpath(".//div[(@id='body') ]"),i));
                try{
                    String res = Wrappers.getElementFromParentElementAndPrintText(driver,latestNewTab, By.xpath(".//span[@id='vote-count-middle']"), i);
                    numberOfVotes += Wrappers.convertToNumericValue(res);
                }
                catch(NoSuchElementException e){
                    System.out.println("Vote not present - "+e.getMessage());
                }
            }
            System.out.println(numberOfVotes);
        }else {
            int numberOfVotes =0;
            for(int i=0;i<3;i++){
                System.out.println("News header"+ i +": "+ Wrappers.getElementFromParentElementAndPrintText(driver,latestNewTab,By.xpath(".//div[(@id='header') ]"),i));
                System.out.println("News Body"+ i +": "+ Wrappers.getElementFromParentElementAndPrintText(driver,latestNewTab,By.xpath(".//div[(@id='body') ]"),i));
                try{
                    String res = Wrappers.getElementFromParentElementAndPrintText(driver,latestNewTab, By.xpath(".//span[@id='vote-count-middle']"), i);
                    numberOfVotes += Wrappers.convertToNumericValue(res);
                }
                catch(NoSuchElementException e){
                    System.out.println("Vote not present - "+e.getMessage());
                }
            }
            System.out.println("Total likes count of top 3 latest news: "+numberOfVotes);
        }
        Wrappers.logStatus("End TestCase", "Test Case 4:", "DONE");
    }


    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterTest
    public void endTest() {
        driver.close();
        driver.quit();

    }
}