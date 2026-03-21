package mission.pages;

import mission.driver.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Base class for all page objects.
 * Provides reusable Selenium actions so page classes never repeat common logic.
 */

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int defaultWait = 20;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultWait));
        PageFactory.initElements(driver, this);
    }

    /**
     * Wait for element to be visible
     */
    protected void waitForElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits for element to be visible then clicks it
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    /**
     * Clears field then types text
     */
    protected void sendKeys(WebElement element, String textToEnter) {
        waitForElementVisible(element);
        element.clear();
        element.sendKeys(textToEnter);
    }

    /**
     * Waits for element to be visible and returns its text
     */
    protected String getText(WebElement element) {
        waitForElementVisible(element);
        return element.getText();
    }

    /**
     * Navigate to the given URL
     */
    protected void navigateTo(String url) {
        driver.get(url);
    }


}
