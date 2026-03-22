package mission.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import mission.config.ConfigReader;
import mission.driver.DriverFactory;
import mission.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Hooks {

    /**
     * Cucumber lifecycle hooks.
     * UI hooks tagged @UI - only run for UI scenarios.
     * API hooks tagged @API - only run for API scenarios.
     * Single Responsibility: test lifecycle management only.
     */


    private static final int PAGE_LOAD_TIMEOUT = 30;

    @Before("@UI")
    public void beforeUIScenario() {
        DriverFactory.initDriver();
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
        driver.manage().window().maximize();
        driver.get(ConfigReader.getProperty("url"));
    }

    @After("@UI")
    public void afterUIScenario(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                ScreenshotUtils.takeScreenshot(driver, scenario.getName());
            }
            DriverFactory.quitDriver();
        }
    }

    @Before("@API")
    public void beforeAPIScenario() {
        // No browser initialization for API tests
        // Add auth setup, token generation or test data here if needed
    }

    @After("@API")
    public void afterAPIScenario(Scenario scenario) {
        // No browser to quit for API tests
        // Add test data cleanup here if needed
        if (scenario.isFailed()) {
            System.out.println("API Scenario failed: " + scenario.getName());
        }
    }
}
