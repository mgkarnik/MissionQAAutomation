package mission.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import mission.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Centralized driver creation and lifecycle management.
 * Single Responsibility: browser setup only.
 * No other class should create a WebDriver instance.
 */

public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void initDriver() {
        String browser = ConfigReader.getProperty("Browser");
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
            case "chromemac":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "chromehedless":
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            default:
                throw new RuntimeException("Browser " + browser + " not supported." +
                        " Check browser value in TestData.properties.");

        }
        driver.manage().window().maximize();
        driverThread.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }

}
