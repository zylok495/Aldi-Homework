package tests;

import org.testng.annotations.Test;
import testdata.UserData;

import static config.TestConfig.REGRESSION;

public class LoginTests extends BaseTest {

    @Test(testName = "Successful login with valid credentials",
            description = "User enters valid email and password - Successful login",
            groups = {REGRESSION})
    public void successfulLogin() {
        dashBoardPage.openLoginModal();
        loginPage.login(UserData.VALID_EMAIL, UserData.VALID_PASSWORD);
        dashBoardPage.assertUserLoggedIn();
    }

    @Test(testName = "Login attempt with incorrect password",
            description = "User enters invalid password",
            groups = {REGRESSION})
    public void incorrectPassword() {
        dashBoardPage.openLoginModal();
        loginPage.login(UserData.VALID_EMAIL, UserData.INVALID_PASSWORD);
        loginPage.waitForLoginFailedMessage();
    }

    //Following test case is written in purpose of handling failures

    @Test(testName = "Catch a failure while login",
            description = "User enters valid email and invalid password for test failure",
            groups = {REGRESSION})
    public void loginForFailure() {
        dashBoardPage.openLoginModal();
        loginPage.login(UserData.VALID_EMAIL, UserData.INVALID_PASSWORD);
        dashBoardPage.assertUserLoggedIn();
    }
}
