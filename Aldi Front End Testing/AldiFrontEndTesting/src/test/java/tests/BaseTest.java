package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import config.TestConfig;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.DashBoardPage;
import pages.LoginPage;

import java.io.ByteArrayInputStream;

public class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected static Playwright playwright;
    protected static Browser browser;
    protected Page page;

    protected DashBoardPage dashBoardPage;
    protected LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        log.info("Starting browser");
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        );
        page = browser.newPage();
        page.navigate(TestConfig.BASE_URL);

        dashBoardPage = new DashBoardPage(page);
        loginPage = new LoginPage(page);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        if (ITestResult.FAILURE == result.getStatus()) {
            takeScreenshotOnFailure();
        }

        log.info("Closing browser");
        page.close();
        browser.close();
        playwright.close();
    }

    private void takeScreenshotOnFailure() {
        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions().setFullPage(true)
        );

        Allure.addAttachment(
                "Failure Screenshot",
                new ByteArrayInputStream(screenshot)
        );
    }

}
