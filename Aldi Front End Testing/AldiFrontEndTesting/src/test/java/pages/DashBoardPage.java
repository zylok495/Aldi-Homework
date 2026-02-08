package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.BaseTest;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class DashBoardPage {

    protected static final Logger log = LoggerFactory.getLogger(DashBoardPage.class);

    private final Page page;

    private final Locator headerLoginButton;
    private final Locator accountLoginButton;
    private final Locator myAccountButton;

    public DashBoardPage(Page page) {
        this.page = page;
        this.headerLoginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in to start shopping"));
        this.accountLoginButton = page.locator("[data-test='account-login']");
        this.myAccountButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My Account"));
    }

    @Step("Open login modal")
    public void openLoginModal() {
        log.info("Opening login modal");
        headerLoginButton.click();
        accountLoginButton.click();
    }

    @Step("Assert that the login was successful")
    public void assertUserLoggedIn() {
        log.info("Asserting successful login");
        assertThat(myAccountButton).isVisible();
    }

}
