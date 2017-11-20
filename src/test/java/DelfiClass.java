
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Andrei Pazniak on 4/22/2017.
 */
public class DelfiClass {
    private static final By COUNTER = By.className("comment-count");
    private static final Logger LOGGER = Logger.getLogger(DelfiClass.class);
    private int commentCount;

    @Test
    public void commentTesting() throws InterruptedException {
        LOGGER.info("We are starting our test");
        WebDriver driver = this.getDriver();
        driver.manage().timeouts().implicitlyWait(10L, TimeUnit.SECONDS);
        LOGGER.info("We are opening rus.delfi.lv home page");
        driver.get("http://rus.delfi.lv");
        LOGGER.info("Getting comment count for the first article");
        this.commentCount = this.getCommentCount(driver).intValue();
        LOGGER.info("Open first article");
        driver.findElement(By.xpath("(//*[contains(@class, 'top2012-title')])[2]")).click();
        LOGGER.info("Getting comment count from title on article page");
        Assert.assertEquals("Wrong comment count on article page", (float)this.commentCount, (float)this.getCommentCount(driver).intValue(), 0.0F);
        LOGGER.info("Comment count on article page is correct!");
        LOGGER.info("Moving to article comment page");
        driver.findElement(By.className("comment-count")).click();
        LOGGER.info("Getting registered users comment count");
        WebElement registeredCountParent = driver.findElement(By.cssSelector("a[class='comment-thread-switcher-list-a comment-thread-switcher-list-a-reg']"));
        WebElement registeredCount = registeredCountParent.findElement(By.xpath("span"));
        String counterRegisteredString = registeredCount.getText();
        Integer countRegisteredUsers = Integer.valueOf(Integer.parseInt(counterRegisteredString.substring(1, counterRegisteredString.length() - 1)));
        LOGGER.info("Article comment count is " + countRegisteredUsers);
        LOGGER.info("Getting unregistered users comment count");
        WebElement unregisteredCountParent = driver.findElement(By.cssSelector("a[class='comment-thread-switcher-list-a comment-thread-switcher-list-a-anon']"));
        WebElement unregisteredCount = unregisteredCountParent.findElement(By.xpath("span"));
        String counterUnregisteredString = unregisteredCount.getText();
        Integer countUnregisteredUsers = Integer.valueOf(Integer.parseInt(counterUnregisteredString.substring(1, counterUnregisteredString.length() - 1)));
        LOGGER.info("Article comment count is " + countUnregisteredUsers);
        LOGGER.info("Checking whole comment count");
        Assert.assertEquals("Wrong comment count on article page", (float)this.commentCount, (float)(countRegisteredUsers.intValue() + countUnregisteredUsers.intValue()), 0.0F);
    }

    private Integer getCommentCount(WebDriver driver) {
        WebElement counter = driver.findElement(COUNTER);
        String counterString = counter.getText();
        Integer count = Integer.valueOf(Integer.parseInt(counterString.substring(1, counterString.length() - 1)));
        LOGGER.info("Article comment count is " + count);
        return count;
    }

    private WebDriver getDriver() {
        LOGGER.info("Setting global property for driver");
        System.setProperty("webdriver.gecko.driver", "C:/geckodriver.exe");
        LOGGER.info("Opening FireFox browser");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();
        return driver;
    }

}
