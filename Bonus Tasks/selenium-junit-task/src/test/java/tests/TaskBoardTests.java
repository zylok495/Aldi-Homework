package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static config.ConfigConstans.BASE_URL;
import static org.junit.jupiter.api.Assertions.*;

public class TaskBoardTests {

    private WebDriver driver;
    private final By TASK_ROWS = By.cssSelector(".task-row");
    private final By TASK_TITLES = By.cssSelector(".task-title");

    @BeforeEach
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    public int getTableTaskRowSize() {
        return driver.findElements(TASK_ROWS).size();
    }

    @Test
    public void deleteTaskFromList() {
        int taskTableRowCount = getTableTaskRowSize();
        //assume that tasks names are unique
        String firstTaskTitle = driver.findElements(TASK_TITLES).get(0).getText();
        WebElement deleteButton = driver.findElement(By.xpath("//button[contains(text(), 'Delete')]"));

        assertTrue(deleteButton.isDisplayed());
        deleteButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(5)). until(ExpectedConditions.numberOfElementsToBeLessThan(
                TASK_ROWS, taskTableRowCount
        ));

        Integer tableRowCountAfterDelete = getTableTaskRowSize();
        assertEquals(taskTableRowCount -1, tableRowCountAfterDelete);

        List<String> taskNames = driver.findElements(TASK_TITLES).stream().map(taskName -> taskName.getText()).toList();
        assertFalse(taskNames.contains(firstTaskTitle));
    }
}
