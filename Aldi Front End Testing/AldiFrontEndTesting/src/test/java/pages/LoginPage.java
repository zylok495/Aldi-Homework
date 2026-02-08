package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {

    protected static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    private final Page page;

    private final Locator emailField;
    private final Locator passwordField;
    private final Locator loginFailedMessage;

    public LoginPage(Page page) {
        this.page = page;
        this.emailField = page.getByLabel("Email");
        this.passwordField = page.getByPlaceholder("Your password");
        this.loginFailedMessage = page.getByRole(AriaRole.ALERT);
    }

    @Step("User logs in with email: {emailAddress}")
    public void login(String emailAddress, String password) {
        log.info("Typing email and password");
        emailField.fill(emailAddress);
        passwordField.fill(password);
        log.info("Submit login");
        passwordField.press("Enter");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Step("Wait for login failed message")
    public void waitForLoginFailedMessage() {
        log.info("Wait for login failed message");
        assertThat(loginFailedMessage).isVisible();
    }

}
