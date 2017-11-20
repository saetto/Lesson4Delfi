import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrei Pazniak on 4/22/2017.
 */
public class TestingMobileDelfi {
    private static final Logger LOGGER = Logger.getLogger(DelfiClass.class);

    @Test
    public void Top5() throws InterruptedException {
        LOGGER.info("We are starting our test");

        WebDriver driver = getDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        LOGGER.info("We are opening rus.delfi.lv home page");
        driver.get("http://rus.delfi.lv");

        List<WebElement> webArticles = driver.findElements(By.cssSelector("h3[class='top2012-title']"));
        List<String> topicList = new ArrayList<String>();
        List<Integer> topicComment = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            WebElement web1 = webArticles.get(i).findElement(By.className("top2012-title"));
            topicList.add(i, web1.getText());
            List<WebElement> commentList = webArticles.get(i).findElements(By.className("comment-count"));
            if (commentList.size() == 0)
                topicComment.add(i, 0);
            if (commentList.size() == 1) {
                String commentListText = commentList.get(0).getText().replace("(", "").replace(")", "");
                int commentArticle = Integer.parseInt(commentListText);
                topicComment.add(i, commentArticle);
            }

            Assert.assertFalse("Wrong number of comments", commentList.size() >= 2);

            //topicComent.add(i, webArticles.get(i).findElement(By.className("comment-count")).getText());
        }
        LOGGER.info("Number of articles is " + webArticles.size());
        for (int i = 0; i < 5; i++) {
            LOGGER.info("Articles with number of comments" + topicList.get(i) + " " + topicComment.get(i));
        }


        ////////////////////////////////////////////////
        LOGGER.info("We are opening m.rus.delfi.lv home page");
        driver.get("http://m.rus.delfi.lv");

        LOGGER.info("We are create massive with 5 articles and their comments");
        List<WebElement> webArticlesMobile = driver.findElements(By.cssSelector("div[class='md-mosaic-title']"));
        List<String> topicListMobile = new ArrayList<String>();
        List<Integer> topicCommentMobile = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            WebElement web1 = webArticlesMobile.get(i).findElement(By.className("md-scrollpos"));
            topicListMobile.add(i, web1.getText());
            List<WebElement> commentListMobile = webArticlesMobile.get(i).findElements(By.className("commentCount"));
            if (commentListMobile.size() == 0)
                topicCommentMobile.add(i, 0);
            if (commentListMobile.size() == 1) {
                String commentListText = commentListMobile.get(0).getText().replace("(", "").replace(")", "");
                int commentArticle = Integer.parseInt(commentListText);
                topicCommentMobile.add(i, commentArticle);
            }
            Assert.assertFalse("Wrong number of comments", commentListMobile.size() >= 2);
        }

        for (int i = 0; i < 5; i++) {
            LOGGER.info("Articles with number of comments" + topicListMobile.get(i) + " " + topicCommentMobile.get(i));
        }
        LOGGER.info("We are compare our articles and comments");
        for (int i=0; i<5; i++){
            Assert.assertEquals("The wrong topic of article",topicList.get(i), topicListMobile.get(i));
            Assert.assertEquals("The wrong number of comment", topicComment.get(i), topicCommentMobile.get(i));
        }
        driver.close();
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
